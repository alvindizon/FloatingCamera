package com.alvindizon.floatingcamera.utils

import android.content.Context
import java.io.File

fun getBitmapCachePath(context: Context): String =
    context.cacheDir.path + File.separator + "bmp"
