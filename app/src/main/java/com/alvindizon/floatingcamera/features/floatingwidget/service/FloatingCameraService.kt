package com.alvindizon.floatingcamera.features.floatingwidget.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.alvindizon.floatingcamera.R
import com.alvindizon.floatingcamera.features.floatingwidget.receiver.StopFloatingCameraReceiver
import com.alvindizon.floatingcamera.features.floatingwidget.screens.FloatingCamera


class FloatingCameraService : Service() {

    private val notificationManager: NotificationManager by lazy {
        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    private lateinit var floatingCamera: FloatingCamera

    override fun onCreate() {
        super.onCreate()
        showFloatingCamera()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.getStringExtra(SVC_COMMAND_KEY) == SVC_COMMAND_EXIT) {
            floatingCamera.removeView()
            notificationManager.cancel(FLOATING_CAMERA_NOTIF_ID)
            stopForeground(STOP_FOREGROUND_REMOVE)
            stopSelf()
            return START_NOT_STICKY
        }
        val notification = createNotification()
        startForeground(FLOATING_CAMERA_NOTIF_ID, notification)
        notificationManager.notify(FLOATING_CAMERA_NOTIF_ID, notification)
        return START_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? = null

    private fun showFloatingCamera() {
        floatingCamera = FloatingCamera(this)
        floatingCamera.initializeView()
    }

    private fun createNotification(): Notification {
        val stopIntent = Intent(applicationContext, StopFloatingCameraReceiver::class.java)
        val stopPendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            STOP_FLOATING_CAMERA_SVC, stopIntent, PendingIntent.FLAG_IMMUTABLE
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                FLOATING_CAMERA_NOTIF_CHANNEL_ID,
                FLOATING_CAMERA_NOTIF_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = FLOATING_CAMERA_CHANNEL_DESC
            }
            notificationManager.createNotificationChannel(channel)
        }

        return NotificationCompat.Builder(this, FLOATING_CAMERA_NOTIF_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_camera)
            .setContentTitle(getString(R.string.app_name))
            .setDeleteIntent(stopPendingIntent)
            .addAction(0, getString(R.string.remove_camera), stopPendingIntent)
            .setOngoing(true)
            .build()
    }


    companion object {
        private const val FLOATING_CAMERA_NOTIF_ID = 18
        private const val FLOATING_CAMERA_NOTIF_CHANNEL_ID = "floating-camera-channel-id"
        private const val STOP_FLOATING_CAMERA_SVC = 42
        private const val FLOATING_CAMERA_NOTIF_CHANNEL_NAME = "FloatingCameraNotifChannel"
        private const val FLOATING_CAMERA_CHANNEL_DESC = "Channel for floating camera"
        const val SVC_COMMAND_KEY = "command-key"
        const val SVC_COMMAND_EXIT = "exit"
    }
}


