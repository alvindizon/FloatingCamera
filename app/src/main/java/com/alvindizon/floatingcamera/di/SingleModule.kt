package com.alvindizon.floatingcamera.di

import com.alvindizon.floatingcamera.features.floatingwidget.screens.FloatingCamera
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val singleModule = module {
    single { FloatingCamera(androidContext()) }
}
