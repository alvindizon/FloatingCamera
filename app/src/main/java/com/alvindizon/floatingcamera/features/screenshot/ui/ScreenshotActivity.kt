package com.alvindizon.floatingcamera.features.screenshot.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.alvindizon.floatingcamera.features.screenshot.viewmodel.ScreenshotContentViewModel
import com.alvindizon.floatingcamera.ui.theme.FloatingCameraTheme
import org.koin.androidx.viewmodel.ext.android.viewModel


class ScreenshotActivity : ComponentActivity() {

    private val viewModel: ScreenshotContentViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.onCreate()
        setContent {
            FloatingCameraTheme {
                ScreenshotScreen(viewModel = viewModel, onCloseButtonClick = { finish() })
            }
        }
    }

    override fun onDestroy() {
        viewModel.onDestroy()
        super.onDestroy()
    }
}
