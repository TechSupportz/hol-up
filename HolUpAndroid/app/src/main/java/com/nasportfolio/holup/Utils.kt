package com.nasportfolio.holup

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.os.Build

fun PackageManager.getAllApplications(): MutableList<ResolveInfo> =
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
    }