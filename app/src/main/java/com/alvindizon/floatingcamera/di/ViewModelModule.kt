package com.alvindizon.floatingcamera.di

import com.alvindizon.floatingcamera.features.screenshot.viewmodel.ScreenshotContentViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { ScreenshotContentViewModel(get()) }
}
