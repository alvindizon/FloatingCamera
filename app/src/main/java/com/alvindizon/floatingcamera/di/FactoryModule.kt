package com.alvindizon.floatingcamera.di

import com.alvindizon.floatingcamera.features.screenshot.ScreenshotManager
import com.alvindizon.floatingcamera.features.screenshot.ScreenshotManagerImpl
import com.alvindizon.floatingcamera.features.screenshot.repo.ScreenshotRepository
import com.alvindizon.floatingcamera.features.screenshot.repo.ScreenshotRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val factoryModule = module {
    factory<ScreenshotRepository> { ScreenshotRepositoryImpl(get(), get()) }
    factory<ScreenshotManager> { ScreenshotManagerImpl(androidContext(), get(), get()) }
}
