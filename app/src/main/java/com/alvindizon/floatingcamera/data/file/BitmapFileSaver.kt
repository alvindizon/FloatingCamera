package com.alvindizon.floatingcamera.data.file

import android.content.Context
import android.graphics.Bitmap
import com.alvindizon.floatingcamera.utils.getBitmapCachePath
import java.io.File
import java.io.FileOutputStream
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

interface BitmapFileSaver {
    suspend fun saveBitmap(bitmap: Bitmap): String
}

/**
 * Class for saving bitmaps.
 */
class BitmapFileSaverImpl(context: Context): BitmapFileSaver {

    private val cacheDirPath: String by lazy {
        getBitmapCachePath(context)
    }

    override suspend fun saveBitmap(bitmap: Bitmap): String = suspendCoroutine { cont ->
        try {
            val key = System.currentTimeMillis().toString(16) + ".bmp"
            val fileFolder = File(cacheDirPath)
            if (!fileFolder.exists()) fileFolder.mkdirs()
            val file = File(cacheDirPath, key)
            if (!file.exists()) file.createNewFile()
            val fos = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
            fos.flush()
            fos.close()
            cont.resume(cacheDirPath + File.separator + key)
        } catch (e: Throwable) {
            e.printStackTrace()
            cont.resumeWithException(e)
        }
    }
}
