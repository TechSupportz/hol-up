package com.nasportfolio.holup.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class HomeButtonWatcher @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val filter: IntentFilter = IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)
    private var listener: OnHomePressedListener? = null
    var receiver: InnerReceiver? = null

    fun setOnHomePressedListener(listener: OnHomePressedListener) {
        this.listener = listener
        receiver = InnerReceiver()
    }

    fun startWatch() {
        receiver ?: return
        context.registerReceiver(receiver, filter)
    }

    fun stopWatch() {
        receiver ?: return
        context.unregisterReceiver(receiver)
    }

    interface OnHomePressedListener {
        fun onHomePressed()
        fun onHomeLongPressed()
    }

    inner class InnerReceiver: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val action = intent?.action ?: return
            if (action != Intent.ACTION_CLOSE_SYSTEM_DIALOGS) return
            val reason = intent.getStringExtra("reason") ?: return
            listener ?: return
            when (reason) {
                "homekey" -> listener?.onHomePressed()
                "recentapps" -> listener?.onHomeLongPressed()
                else -> Unit
            }
        }

    }
}