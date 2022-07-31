package com.alvindizon.floatingcamera.features.floatingwidget.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.alvindizon.floatingcamera.features.floatingwidget.service.FloatingCameraService

class StopFloatingCameraReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val exitIntent = Intent(context.applicationContext, FloatingCameraService::class.java)
        exitIntent.putExtra(
            FloatingCameraService.SVC_COMMAND_KEY,
            FloatingCameraService.SVC_COMMAND_EXIT
        )
        context.startForegroundService(exitIntent)
    }
}
