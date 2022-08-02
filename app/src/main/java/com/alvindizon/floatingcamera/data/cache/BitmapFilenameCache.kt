package com.alvindizon.floatingcamera.data.cache

import androidx.annotation.VisibleForTesting
import java.io.File

/**
 * Class for storing bitmap filenames.
 */
interface BitmapFilenameCache {
    fun getLatestScreenshotFilename(): String?
    suspend fun saveFilename(filename: String)
    fun clearCache()
}

class BitmapFilenameCacheImpl : BitmapFilenameCache {

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val fileNameList = mutableListOf<String>()

    override fun getLatestScreenshotFilename() = fileNameList.getOrNull(fileNameList.size - 1)

    override suspend fun saveFilename(filename: String) {
        fileNameList.add(filename)
    }

    override fun clearCache() {
        fileNameList.forEach {
            val file = File(it)
            file.deleteRecursively()
        }
        fileNameList.clear()
    }
}
