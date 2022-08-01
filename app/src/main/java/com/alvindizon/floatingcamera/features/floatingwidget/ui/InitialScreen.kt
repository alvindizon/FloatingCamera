package com.alvindizon.floatingcamera.features.floatingwidget.ui

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.alvindizon.floatingcamera.R

@Composable
fun InitialScreen(onOverlayPermissionGranted: () -> Unit) {
    val context = LocalContext.current

    val showDialog = rememberSaveable { mutableStateOf(false) }

    if (showDialog.value) {
        PermissionDialog(
            onDismiss = { showDialog.value = false },
            onOkClick = {
                context.startActivity(
                    Intent(
                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:${context.packageName}")
                    )
                )
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        InitialScreenContent(modifier = Modifier.align(Alignment.Center)) {
            // show dialog if app can't draw over other apps
            showDialog.value = !Settings.canDrawOverlays(context)
            if (!showDialog.value) {
                // invoke callback so that activity can ask for screen capture permission
                onOverlayPermissionGranted()
            }
        }
    }

}

@Composable
fun InitialScreenContent(modifier: Modifier = Modifier, onButtonClick: () -> Unit) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            textAlign = TextAlign.Center,
            text = stringResource(id = R.string.initial_screen_content_title),
            style = MaterialTheme.typography.subtitle1
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onButtonClick) {
            Text(
                text = stringResource(id = R.string.initial_screen_content_button_msg),
                style = MaterialTheme.typography.subtitle2
            )
        }
    }
}

@Composable
fun PermissionDialog(
    onDismiss: () -> Unit,
    onOkClick: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    onOkClick.invoke()
                    onDismiss.invoke()
                }
            ) {
                Text(text = stringResource(id = R.string.initial_screen_button_label))
            }
        },
        title = { Text(text = stringResource(id = R.string.initial_screen_dialog_title)) },
        text = { Text(text = stringResource(id = R.string.initial_screen_permission_msg)) }
    )
}
