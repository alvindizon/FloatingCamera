package com.alvindizon.floatingcamera.data.cache

import android.content.Context
import android.graphics.Bitmap
import com.alvindizon.floatingcamera.utils.getBitmapCachePath
import java.io.File
import java.io.FileOutputStream
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * Class for saving bitmaps and for storing their filenames.
 */
interface BitmapFilenameCache {
    fun getLatestScreenshotFilename(): String?
    suspend fun saveBitmap(bitmap: Bitmap)
    fun clearCache()
}

class BitmapFilenameCacheImpl(context: Context) : BitmapFilenameCache {

    private val cacheDirPath: String by lazy {
        getBitmapCachePath(context)
    }

    private val fileNameList = mutableListOf<String>()

    override fun getLatestScreenshotFilename() = fileNameList.getOrNull(fileNameList.size - 1)

    override suspend fun saveBitmap(bitmap: Bitmap) = suspendCoroutine { cont ->
        try {
            val key = System.currentTimeMillis().toString(16) + ".bmp"
            val fileFolder = File(cacheDirPath)
            if (!fileFolder.exists()) fileFolder.mkdirs()
            val file = File(cacheDirPath, key)
            if (!file.exists()) file.createNewFile()
            val fos = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
            fileNameList.add(cacheDirPath + File.separator + key)
            fos.flush()
            fos.close()
            cont.resume(Unit)
        } catch (e: Throwable) {
            e.printStackTrace()
            cont.resumeWithException(e)
        }
    }

    override fun clearCache() {
        fileNameList.forEach {
            val file = File(it)
            file.deleteRecursively()
        }
    }
}
