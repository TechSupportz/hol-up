package com.nasportfolio.holup.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.nasportfolio.holup.startBlockerService

class BootUpReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        context?.startBlockerService(
            navigate = false
        )
    }
}