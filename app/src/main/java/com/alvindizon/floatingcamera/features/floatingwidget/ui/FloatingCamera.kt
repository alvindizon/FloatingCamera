package com.alvindizon.floatingcamera.features.floatingwidget.ui

import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.ComposeView
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewTreeLifecycleOwner
import androidx.lifecycle.ViewTreeViewModelStoreOwner
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import kotlin.math.roundToInt


class FloatingCamera(context: Context) {
    // explanation of what wm does: https://stackoverflow.com/questions/19846541/what-is-windowmanager-in-android
    // more refs; https://levelup.gitconnected.com/add-view-outside-activity-through-windowmanager-1a70590bad40
    // it is responsible for managing how windows are laid out on screen, as well as transitions/animations
    // for example: an activity has a Window containing its UI. Calling setContentView attaches the View
    // to its default window, and then WM will do whatever is necessary to display whichever window is on top
    private val windowManager: WindowManager by lazy {
        context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    }

    private val view = ComposeView(context)

    // creating a floating view with an Activity isn't feasible as the Activity will stop,
    // and the floating view will be destroyed, so instead, use a service to interact with WM
    // in order to display a floating view that can overlay over other apps
    // this flag is for displaying on top of other application windows (for devices >= API 26)
    // this flag needs SYSTEM_ALERT_WINDOW permission to work
    private val layoutFlag: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
    } else {
    // we can also use WindowManager.LayoutParams.TYPE_SYSTEM_ALERT here. difference between
    // TYPE_PHONE and TYPE_SYSTEM_ALERT: https://stackoverflow.com/questions/27961399/android-type-phone-vs-type-system-alert
        @Suppress("DEPRECATION")
        WindowManager.LayoutParams.TYPE_PHONE
    }
    // WRAP_CONTENT so that the window wraps the content rather than filling the screen
    // FLAG_NOT_FOCUSABLE: The window won’t ever get key input focus, so the user can not send key or other button events to it.
    // Those will instead go to whatever focusable window is behind it.
    // This one is extremely important, because it allows us to control the apps behind the floating window.
    // FLAG_NOT_TOUCH_MODAL: Allow any pointer events outside of the window to be sent to the windows behind it (might not be necessary)
    // PixelFormat.TRANSLUCENT: Make the underlying application window visible through any transparent parts
    private val params = WindowManager.LayoutParams(
        WindowManager.LayoutParams.WRAP_CONTENT,
        WindowManager.LayoutParams.WRAP_CONTENT,
        layoutFlag,
        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
        PixelFormat.TRANSLUCENT
    )

    fun initializeView(onClick: () -> Unit) {
        view.setContent {
            FloatingCamera(
                onDrag = { x, y ->
                    // we need to send updates to UI as we drag the icon
                    params.x += x.roundToInt()
                    params.y += y.roundToInt()
                    windowManager.updateViewLayout(view, params)
                },
                onClick = onClick
            )
        }

        // provide a fake lifecycleowner so we can use composables
        // ref: https://gist.github.com/handstandsam/6ecff2f39da72c0b38c07aa80bbb5a2f
        val viewModelStore = ViewModelStore()
        val lifecycleOwner = FloatingCameraLifeycleOwner()
        lifecycleOwner.performRestore(null)
        lifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
        ViewTreeLifecycleOwner.set(view, lifecycleOwner)
        ViewTreeViewModelStoreOwner.set(view) { viewModelStore }
        view.setViewTreeSavedStateRegistryOwner(lifecycleOwner)
        windowManager.addView(view, params)
    }

    fun removeView() {
        windowManager.removeView(view)
    }

    fun toggleVisibility(isVisible: Boolean) {
        view.isVisible = isVisible
    }
}


@Composable
fun FloatingCamera(onDrag: (Float, Float) -> Unit, onClick: () -> Unit) {
    // ref: https://developer.android.com/jetpack/compose/gestures#dragging
    FloatingActionButton(
        onClick = onClick,
        modifier = Modifier
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { change, dragAmount ->
                        change.consume()
                        onDrag(dragAmount.x, dragAmount.y)
                    }
                )
            }
    ) {
        Icon(
            imageVector = Icons.Outlined.PhotoCamera,
            contentDescription = null,
            tint = Color.White
        )
    }
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
