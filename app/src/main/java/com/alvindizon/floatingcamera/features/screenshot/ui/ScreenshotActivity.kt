package com.alvindizon.floatingcamera.features.screenshot.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import coil.compose.rememberAsyncImagePainter
import com.alvindizon.floatingcamera.features.screenshot.repo.ScreenshotRepository
import org.koin.android.ext.android.inject
import java.io.File


class ScreenshotActivity : ComponentActivity() {

    private val screenshotRepository: ScreenshotRepository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val filePath = screenshotRepository.getLatestScreenshotFilename()
        val cacheFile = File(filePath)
        setContent {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(model = cacheFile),
                        contentDescription = null
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        screenshotRepository.release()
        super.onDestroy()
    }
}
