package com.alvindizon.floatingcamera.di

import com.alvindizon.floatingcamera.data.cache.BitmapFilenameCache
import com.alvindizon.floatingcamera.data.cache.BitmapFilenameCacheImpl
import com.alvindizon.floatingcamera.features.floatingwidget.ui.FloatingCamera
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val singleModule = module {
    single { FloatingCamera(androidContext()) }
    single<BitmapFilenameCache> { BitmapFilenameCacheImpl(androidContext()) }
}
