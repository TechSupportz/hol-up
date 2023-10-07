package com.nasportfolio.holup

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.Bitmap
import android.os.Build
import androidx.core.graphics.drawable.toBitmap

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