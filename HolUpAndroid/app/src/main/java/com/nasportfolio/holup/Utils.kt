package com.nasportfolio.holup

import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Process
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.nasportfolio.holup.service.BlockerService

data class ApplicationInfo(
    val name: String,
    val icon: Bitmap,
    val packageName: String,
    val moreInfo: ResolveInfo,
)

fun PackageManager.getAllApplications(): MutableList<ApplicationInfo> =
    Intent(Intent.ACTION_MAIN, null).run {
        addCategory(Intent.CATEGORY_LAUNCHER)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            queryIntentActivities(
                this,
                PackageManager.ResolveInfoFlags.of(0L)
            )
        } else {
            queryIntentActivities(this, 0)
        }
    }.map {
        ApplicationInfo(
            name = it.activityInfo.loadLabel(this).toString(),
            packageName = it.activityInfo.packageName,
            icon = it.activityInfo.loadIcon(this).toBitmap(),
            moreInfo = it
        )
    }.toMutableList()

fun Context.checkUsageStatsPermission(): Boolean {
    val appOpsManager = getSystemService(ComponentActivity.APP_OPS_SERVICE) as AppOpsManager
    val mode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        appOpsManager.unsafeCheckOpNoThrow(
            "android:get_usage_stats",
            Process.myUid(),
            packageName
        )
    } else {
        appOpsManager.checkOpNoThrow(
            "android:get_usage_stats",
            Process.myUid(),
            packageName
        )
    }
    return mode == AppOpsManager.MODE_ALLOWED
}

fun Context.checkDrawOverOtherAppsPermission(): Boolean {
    return Settings.canDrawOverlays(this)
}

fun Context.startBlockerService(navigate: Boolean = true) {
    if (!checkUsageStatsPermission()) return run {
        if (!navigate) return@run
        Intent(
            Settings.ACTION_USAGE_ACCESS_SETTINGS,
            Uri.parse("package:$packageName")
        ).apply {
            startActivity(this)
        }
    }
    if (!checkDrawOverOtherAppsPermission()) return run {
        if (!navigate) return@run
        Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:$packageName")
        ).apply {
            startActivity(this)
        }
    }
    ContextCompat.startForegroundService(
        this,
        Intent(this, BlockerService::class.java)
    )
}