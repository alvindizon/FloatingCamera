package com.alvindizon.floatingcamera.features.floatingwidget.screens

import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewTreeLifecycleOwner
import androidx.lifecycle.ViewTreeViewModelStoreOwner
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner

class FloatingCamera(context: Context) {
    val view = ComposeView(context)

    private val layoutFlag: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
    } else {
        @Suppress("DEPRECATION")
        WindowManager.LayoutParams.TYPE_PHONE
    }

    val params = WindowManager.LayoutParams(
        WindowManager.LayoutParams.WRAP_CONTENT,
        WindowManager.LayoutParams.WRAP_CONTENT,
        layoutFlag,
        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
        PixelFormat.TRANSLUCENT
    )

    init {
        view.setContent {
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Outlined.PhotoCamera,
                    modifier = Modifier.size(48.dp),
                    contentDescription = null,
                    tint = Color.DarkGray
                )
            }
        }
        configureView(view)
    }
}

fun configureView(composeView: ComposeView) {
    val viewModelStore = ViewModelStore()
    val lifecycleOwner = FloatingCameraLifeycleOwner()
    lifecycleOwner.performRestore(null)
    lifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
    ViewTreeLifecycleOwner.set(composeView, lifecycleOwner)
    ViewTreeViewModelStoreOwner.set(composeView) { viewModelStore }
    composeView.setViewTreeSavedStateRegistryOwner(lifecycleOwner)
}

internal class FloatingCameraLifeycleOwner : SavedStateRegistryOwner {
    private var mLifecycleRegistry: LifecycleRegistry = LifecycleRegistry(this)
    private var mSavedStateRegistryController: SavedStateRegistryController =
        SavedStateRegistryController.create(this)

    override fun getLifecycle(): Lifecycle {
        return mLifecycleRegistry
    }


    fun handleLifecycleEvent(event: Lifecycle.Event) {
        mLifecycleRegistry.handleLifecycleEvent(event)
    }

    override val savedStateRegistry: SavedStateRegistry =
        mSavedStateRegistryController.savedStateRegistry

    fun performRestore(savedState: Bundle?) {
        mSavedStateRegistryController.performRestore(savedState)
    }
}
