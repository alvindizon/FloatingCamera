package com.alvindizon.floatingcamera.features.screenshot

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.media.ImageReader
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.util.DisplayMetrics
import android.view.Display
import android.view.WindowManager
import androidx.core.hardware.display.DisplayManagerCompat

interface ScreenshotManager {
    fun initialize(mediaData: Intent)
    fun capture(): Bitmap?
    fun release()
}

/**
 * Uses MediaProjection API to capture the screen.
 */
class ScreenshotManagerImpl(
    private val context: Context,
    private val mediaProjectionManager: MediaProjectionManager,
    private val windowManager: WindowManager
) : ScreenshotManager {

    private var screenDensity = 0
    private var windowWidth = 0
    private var windowHeight = 0

    private var mediaData: Intent? = null

    private var imageReader: ImageReader? = null

    private var virtualDisplay: VirtualDisplay? = null

    private var mediaProjection: MediaProjection? = null

    override fun initialize(mediaData: Intent) {
        this.mediaData = mediaData
        // a mediaprojection
        mediaProjection = mediaProjectionManager.getMediaProjection(Activity.RESULT_OK, mediaData)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val displayContext =
                DisplayManagerCompat.getInstance(context).getDisplay(Display.DEFAULT_DISPLAY)?.let {
                    context.createDisplayContext(it)
                }
            displayContext?.apply {
                windowHeight = resources.displayMetrics.heightPixels
                windowWidth = resources.displayMetrics.widthPixels
                screenDensity = resources.displayMetrics.densityDpi
            }

        } else {
            val metrics = DisplayMetrics()
            @Suppress("DEPRECATION")
            windowManager.defaultDisplay.getRealMetrics(metrics)
            windowHeight = metrics.heightPixels
            windowWidth = metrics.widthPixels
            screenDensity = metrics.densityDpi
        }

        imageReader = ImageReader.newInstance(windowWidth, windowHeight, 0x1, 1)

        virtualDisplay = mediaProjection?.createVirtualDisplay(
            "ss-display", windowWidth, windowHeight, screenDensity,
            DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR, imageReader?.surface, null, null
        )
    }

    override fun capture(): Bitmap? {
        return if (virtualDisplay != null && imageReader != null) {
            val image = imageReader!!.acquireLatestImage()
            val width = image.width
            val height = image.height
            val planes = image.planes
            val buffer = planes[0].buffer
            val pixelStride = planes[0].pixelStride
            val rowStride = planes[0].rowStride
            val rowPadding = rowStride - pixelStride * width
            val bitmap =
                Bitmap.createBitmap(
                    width + rowPadding / pixelStride,
                    height,
                    Bitmap.Config.ARGB_8888
                )
            bitmap.copyPixelsFromBuffer(buffer)
            image.close()
            Bitmap.createBitmap(bitmap, 0, 0, windowWidth, windowHeight)
        } else {
            null
        }
    }

    override fun release() {
        virtualDisplay?.release()
        virtualDisplay = null
        mediaProjection?.stop()
    }
}
