package com.alvindizon.floatingcamera.di

import android.content.Context
import android.media.projection.MediaProjectionManager
import android.view.WindowManager
import com.alvindizon.floatingcamera.data.cache.BitmapFilenameCache
import com.alvindizon.floatingcamera.data.cache.BitmapFilenameCacheImpl
import com.alvindizon.floatingcamera.features.floatingwidget.ui.FloatingCamera
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val singleModule = module {
    single { FloatingCamera(androidContext()) }
    single<BitmapFilenameCache> { BitmapFilenameCacheImpl() }
    single { androidContext().getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager }
    single { androidContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager }
}
