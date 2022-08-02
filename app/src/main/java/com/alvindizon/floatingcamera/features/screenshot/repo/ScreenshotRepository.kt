package com.alvindizon.floatingcamera.features.screenshot.repo

import android.content.Intent
import android.graphics.Bitmap
import com.alvindizon.floatingcamera.data.cache.BitmapFilenameCache
import com.alvindizon.floatingcamera.data.file.BitmapFileSaver
import com.alvindizon.floatingcamera.features.screenshot.ScreenshotManager

interface ScreenshotRepository {
    fun initialize(mediaData: Intent)
    fun capture(): Bitmap?
    suspend fun saveBitmap(bitmap: Bitmap)
    fun getLatestScreenshotFilename(): String?
    fun release()
}

/**
 * Aggregates access to cache and screenshot-related code
 */
class ScreenshotRepositoryImpl(
    private val bitmapFilenameCache: BitmapFilenameCache,
    private val screenshotManager: ScreenshotManager,
    private val bitmapFileSaver: BitmapFileSaver
) : ScreenshotRepository {

    override fun initialize(mediaData: Intent) {
        screenshotManager.initialize(mediaData)
    }

    override fun capture(): Bitmap? = screenshotManager.capture()

    override suspend fun saveBitmap(bitmap: Bitmap) {
        val filename = bitmapFileSaver.saveBitmap(bitmap)
        bitmapFilenameCache.saveFilename(filename)
    }

    override fun getLatestScreenshotFilename(): String? {
        return bitmapFilenameCache.getLatestScreenshotFilename()
    }

    override fun release() {
        bitmapFilenameCache.clearCache()
        screenshotManager.release()
    }
}
