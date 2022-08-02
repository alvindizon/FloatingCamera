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
import com.alvindizon.floatingcamera.features.floatingwidget.ui.FloatingCamera
import com.alvindizon.floatingcamera.features.screenshot.repo.ScreenshotRepository
import com.alvindizon.floatingcamera.features.screenshot.ui.ScreenshotActivity
import com.alvindizon.floatingcamera.utils.getSafeParcelable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject


class FloatingCameraService : Service() {

    private val notificationManager: NotificationManager by lazy {
        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    private val floatingCamera: FloatingCamera by inject()

    private val screenshotRepository: ScreenshotRepository by inject()

    override fun onCreate() {
        super.onCreate()
        val notification = createNotification()
        startForeground(FLOATING_CAMERA_NOTIF_ID, notification)
        notificationManager.notify(FLOATING_CAMERA_NOTIF_ID, notification)
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

        if (intent?.hasExtra(MEDIA_DATA_KEY) == true) {
            // create media projection in screenshot manager thru repo
            val mediaData = intent.getSafeParcelable<Intent>(MEDIA_DATA_KEY)!!
            screenshotRepository.initialize(mediaData)
        }

        return START_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? = null

    override fun onDestroy() {
        screenshotRepository.release()
        super.onDestroy()
    }

    private fun showFloatingCamera() {
        floatingCamera.initializeView (onClick = { capture() })
    }

    private fun capture() {
        GlobalScope.launch(Dispatchers.Main) {
            floatingCamera.toggleVisibility(false)
            // add a delay so that floating icon is invisible before screenshot
            delay(100L)
            val bitmap = screenshotRepository.capture()
            val job = launch  {
                bitmap?.let { screenshotRepository.saveBitmap(it) }
            }
            // block until saving is complete
            job.join()
            // make floating icon visible again after bitmap is saved
            floatingCamera.toggleVisibility(true)
            val intent = Intent(this@FloatingCameraService, ScreenshotActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }
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
        const val MEDIA_DATA_KEY = "mediaData-key"
    }
}


