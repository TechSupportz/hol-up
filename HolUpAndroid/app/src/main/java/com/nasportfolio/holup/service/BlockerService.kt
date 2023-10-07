package com.nasportfolio.holup.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.nasportfolio.holup.R
import com.nasportfolio.holup.TakeABreakActivity
import com.nasportfolio.holup.dao.BlockedAppDao
import com.nasportfolio.holup.models.BlockedApp
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.Date
import javax.inject.Inject


@AndroidEntryPoint
class BlockerService : Service() {
    @Inject
    lateinit var dao: BlockedAppDao

    private val interval = 1000L

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    override fun onBind(p0: Intent?): IBinder? {
        throw UnsupportedOperationException()
    }

    override fun onCreate() {
        super.onCreate()
        val channelId = "Hol-Up-10"
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
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
        checkCurrentApp()
    }

    private fun checkCurrentApp() {
        scope.launch {
            while (isActive) {
                val blockedApps = dao.getAllBlockedApps().first()
                getCurrentApp(blockedApps)
                delay(interval)
            }
        }
    }

    private fun getCurrentApp(blockedApps: List<BlockedApp>) {
        val usageStatsManager =
            getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val currentTime = System.currentTimeMillis()
        val usageEvents = usageStatsManager.queryEvents(currentTime - interval, currentTime)
        val event = UsageEvents.Event()
        while (usageEvents.hasNextEvent()) {
            usageEvents.getNextEvent(event)
            if (event.eventType == UsageEvents.Event.ACTIVITY_RESUMED) {
                val index = blockedApps.map { it.packageName }.indexOf(event.packageName)
                if (index == -1) continue
                val blockedApp = blockedApps[index]
                if (!blockedApp.startAccumulatingTime) {
                    val launchIntent =
                        packageManager.getLaunchIntentForPackage(packageName)
                    launchIntent?.let { startActivity(it) }
                    Intent(this, TakeABreakActivity::class.java).also {
                        it.addFlags(FLAG_ACTIVITY_NEW_TASK)
                        it.putExtra("package", event.packageName)
                        println("STARTING ACTIVITY")
                        startActivity(it)
                    }
                } else {
                    val lastUpdated = blockedApp.lastUpdated
                    val timePassed = currentTime - lastUpdated
                    val timePassedInMinutes = ((timePassed / 1000.0) / 60.0)
                    runBlocking {
                        dao.upsertBlockedApp(
                            blockedApp = blockedApp.copy(
                                lastUpdated = Date().time,
                                timeUsed = blockedApp.timeUsed + timePassedInMinutes,
                                startAccumulatingTime = blockedApp.timeUsed + timePassedInMinutes < blockedApp.timeAllowed
                            )
                        )
                    }
                }
            } else if (event.eventType == UsageEvents.Event.ACTIVITY_STOPPED) {
                runBlocking {
                    val index = blockedApps.map { it.packageName }.indexOf(event.packageName)
                    if (index == -1) return@runBlocking
                    dao.upsertBlockedApp(
                        blockedApp = blockedApps[index].copy(
                            startAccumulatingTime = false
                        )
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

}