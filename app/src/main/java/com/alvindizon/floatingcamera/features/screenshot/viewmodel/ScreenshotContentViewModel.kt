package com.alvindizon.floatingcamera.features.screenshot.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.alvindizon.floatingcamera.features.screenshot.repo.ScreenshotRepository
import com.alvindizon.floatingcamera.utils.IntentUtils
import java.io.File

class ScreenshotContentViewModel(private val screenshotRepository: ScreenshotRepository) :
    ViewModel() {

    var cacheFile: File? = null

    fun onCreate() {
        val filePath = screenshotRepository.getLatestScreenshotFilename()
        cacheFile = filePath?.let { File(it) }
    }

    fun onShareButtonClick(context: Context) {
        cacheFile?.let { IntentUtils.shareFile(context, it) }
    }

    fun onEmailButtonClick(context: Context) {
        cacheFile?.let { IntentUtils.emailFile(context, it) }
    }

    fun onDestroy() {
        screenshotRepository.release()
    }
}
