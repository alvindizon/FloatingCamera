package com.alvindizon.floatingcamera

import android.app.Activity
import android.content.Intent
import android.media.projection.MediaProjectionManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.alvindizon.floatingcamera.features.floatingwidget.screens.InitialScreen
import com.alvindizon.floatingcamera.features.floatingwidget.service.FloatingCameraService
import com.alvindizon.floatingcamera.ui.theme.FloatingCameraTheme

class MainActivity : ComponentActivity() {

    private val startMediaProjection = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            val intent = Intent(applicationContext, FloatingCameraService::class.java)
            // send the resulting intent to screenshotmanager by way of FloatingCameraService
            // so that mediaprojection can be initialized
            intent.putExtra(FloatingCameraService.MEDIA_DATA_KEY, it.data)
            ContextCompat.startForegroundService(this, intent)
        } else {
            Toast.makeText(this, getString(R.string.screen_capture_not_granted_msg), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mediaProjectionManager: MediaProjectionManager = getSystemService(MediaProjectionManager::class.java)

        setContent {
            FloatingCameraTheme {
                InitialScreen {
                    // ask for permission to record/capture screen
                    startMediaProjection.launch(mediaProjectionManager.createScreenCaptureIntent())
                }
            }
        }
    }
}
