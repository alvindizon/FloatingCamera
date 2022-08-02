package com.alvindizon.floatingcamera

import android.app.Application
import com.alvindizon.floatingcamera.di.factoryModule
import com.alvindizon.floatingcamera.di.singleModule
import com.alvindizon.floatingcamera.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class FloatingCameraApp: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@FloatingCameraApp)
            modules(listOf(singleModule, factoryModule, viewModelModule))
        }
    }
}
