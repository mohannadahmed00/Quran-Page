package com.giraffe.quranpage.common.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.giraffe.quranpage.common.service.PlaybackService

class MediaPlayerReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val serviceIntent = Intent(context, PlaybackService::class.java).apply {
            action = intent.action
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(serviceIntent)
        } else context.startService(serviceIntent)
    }

}