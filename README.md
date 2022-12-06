# FloatingCamera

Demonstrates a movable, floating button using Jetpack Compose and Coroutines.
- The button, when enabled, floats above the current UI and can be dragged/moved.
- Clicking the floating button takes a screenshot of the current screen.
- This screenshot can be shared via email or by other options.

This project uses:
1. [Jetpack Compose](https://developer.android.com/jetpack/compose) for the UI
2. [Coroutines](https://github.com/Kotlin/kotlinx.coroutines)
3. [Koin](https://github.com/InsertKoinIO/koin) for dependency injection
4. [MediaProjection](https://developer.android.com/reference/android/media/projection/MediaProjection) for taking screenshots

References used:
- https://localazy.com/blog/floating-windows-on-android-1-jetpack-compose-room (Part 1 of 10 of a series)
- https://gist.github.com/handstandsam/6ecff2f39da72c0b38c07aa80bbb5a2f
- https://github.com/tberghuis/FloatingCountdownTimer
- https://github.com/Fate-Grand-Automata/FGA
- https://github.com/ekibun/Stitch
- https://github.com/tateisu/ScreenShotButton
