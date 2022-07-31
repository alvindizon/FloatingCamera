package com.alvindizon.floatingcamera

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.alvindizon.floatingcamera.features.floatingwidget.screens.InitialScreen
import com.alvindizon.floatingcamera.ui.theme.FloatingCameraTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FloatingCameraTheme {
                InitialScreen()
            }
        }
    }
}
