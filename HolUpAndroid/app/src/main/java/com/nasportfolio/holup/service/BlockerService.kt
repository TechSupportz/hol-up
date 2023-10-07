package com.nasportfolio.holup.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.nasportfolio.holup.R
import com.nasportfolio.holup.data.dao.BlockedAppDao
import com.nasportfolio.holup.data.models.BlockedApp
import com.nasportfolio.holup.ui.blocker.Window
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@AndroidEntryPoint
class BlockerService : Service() {
    @Inject
    lateinit var dao: BlockedAppDao

    @Inject
    lateinit var window: Window

    @Inject
    lateinit var homeButtonWatcher: HomeButtonWatcher

    private val interval = 1000L

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    override fun onBind(p0: Intent?): IBinder? {
        throw UnsupportedOperationException()
    }

    override fun onCreate() {
        super.onCreate()
        window.onCreate()
        homeButtonWatcher.setOnHomePressedListener(object :
            HomeButtonWatcher.OnHomePressedListener {
            override fun onHomePressed() {
                if (!window.isShown()) return
                window.hide()
            }

            override fun onHomeLongPressed() {
                if (!window.isShown()) return
                window.hide()
            }
        })
        homeButtonWatcher.startWatch()
        scope.launch {
            while (isActive) {
                val blockedApps = dao.getAllBlockedApps().first()
                getCurrentApp(blockedApps)
                delay(interval)
            }
        }
    }

    private suspend fun getCurrentApp(blockedApps: List<BlockedApp>) {
        val usageStatsManager = getSystemService(
            Context.USAGE_STATS_SERVICE
        ) as UsageStatsManager
        val currentTime = System.currentTimeMillis()
        val usageEvents = usageStatsManager.queryEvents(currentTime - interval, currentTime)
        val event = UsageEvents.Event()
        while (usageEvents.hasNextEvent()) {
            usageEvents.getNextEvent(event)
            blockedApps.forEach { blockedApp ->
                if (blockedApp.packageName != event.packageName) return@forEach
                withContext(Dispatchers.Main) {
                    if (event.eventType == UsageEvents.Event.ACTIVITY_RESUMED) {
                        println("SHOWING")
                        window.show()
                        return@withContext
                    }
                    if (event.eventType == UsageEvents.Event.ACTIVITY_STOPPED) {
                        println("HIDING")
                        window.hide()
                        return@withContext
                    }
                }
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val channelId = "Hol-Up-10"
        val notificationManager = getSystemService(
            Context.NOTIFICATION_SERVICE
        ) as NotificationManager
        val channel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel(
                channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT
            ).also {
                notificationManager.createNotificationChannel(it)
            }
        } else {
            null
        }
        val builder = channel?.let {
            NotificationCompat.Builder(this, channelId)
        } ?: NotificationCompat.Builder(this)
        val notification = builder
            .setContentTitle("Hol-Up")
            .setContentText("Blocking your apps now")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setAutoCancel(false)
            .build()
        startForeground(1, notification)
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
        window.onDestroy()
        homeButtonWatcher.stopWatch()
    }

}