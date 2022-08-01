package com.alvindizon.floatingcamera.features.screenshot.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import com.alvindizon.floatingcamera.features.screenshot.repo.ScreenshotRepository
import com.alvindizon.floatingcamera.ui.theme.FloatingCameraTheme
import org.koin.android.ext.android.inject
import java.io.File


class ScreenshotActivity : ComponentActivity() {

    private val screenshotRepository: ScreenshotRepository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val filePath = screenshotRepository.getLatestScreenshotFilename()
        val cacheFile = File(filePath)
        setContent {
            FloatingCameraTheme {
                ScreenshotContent(
                    cacheFile = cacheFile,
                    onShareButtonClick = {}, onEmailButtonClick = {}
                )
            }
        }
    }

    override fun onDestroy() {
        screenshotRepository.release()
        super.onDestroy()
    }
}
