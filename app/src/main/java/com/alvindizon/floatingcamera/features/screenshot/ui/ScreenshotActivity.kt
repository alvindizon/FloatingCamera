package com.alvindizon.floatingcamera.features.screenshot.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.content.FileProvider
import com.alvindizon.floatingcamera.R
import com.alvindizon.floatingcamera.features.screenshot.repo.ScreenshotRepository
import com.alvindizon.floatingcamera.ui.theme.FloatingCameraTheme
import com.alvindizon.floatingcamera.utils.getFileProviderAuthority
import org.koin.android.ext.android.inject
import java.io.File


class ScreenshotActivity : ComponentActivity() {

    private val screenshotRepository: ScreenshotRepository by inject()

    private var cacheFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val filePath = screenshotRepository.getLatestScreenshotFilename()
        cacheFile = filePath?.let { File(it) }
        setContent {
            FloatingCameraTheme {
                ScreenshotContent(
                    cacheFile = cacheFile,
                    onShareButtonClick = { share() },
                    onEmailButtonClick = {},
                    onCloseButtonClick = { finish() }
                )
            }
        }
    }

    override fun onDestroy() {
        screenshotRepository.release()
        super.onDestroy()
    }

    private fun share() {
        cacheFile?.let {
            val uri = FileProvider.getUriForFile(this, getFileProviderAuthority(this), it)
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                setDataAndType(uri, "image/png")
                putExtra(Intent.EXTRA_STREAM, uri)
            }
            startActivity(Intent.createChooser(shareIntent, getString(R.string.app_name)))
        }
    }
}
