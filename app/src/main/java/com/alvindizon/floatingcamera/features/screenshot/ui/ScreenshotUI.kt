package com.alvindizon.floatingcamera.features.screenshot.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.alvindizon.floatingcamera.R
import java.io.File

@Composable
fun ScreenshotContent(
    cacheFile: File?,
    onShareButtonClick: () -> Unit,
    onEmailButtonClick: () -> Unit,
    onCloseButtonClick: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier.align(Alignment.Center),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconButton(onClick = onCloseButtonClick, modifier = Modifier.align(Alignment.Start)) {
                Icon(imageVector = Icons.Filled.Close, contentDescription = null)
            }
            Spacer(modifier = Modifier.height(8.dp))
            if (cacheFile != null) {
                Image(
                    modifier = Modifier.weight(1f),
                    painter = rememberAsyncImagePainter(model = cacheFile),
                    contentDescription = null
                )
            } else {
                Text(
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    text = stringResource(id = R.string.screenshot_error_msg),
                    style = MaterialTheme.typography.subtitle1
                )
            }
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onShareButtonClick, modifier = Modifier.weight(1f)) {
                    Image(imageVector = Icons.Filled.Share, contentDescription = null)
                }
                IconButton(onClick = onEmailButtonClick, modifier = Modifier.weight(1f)) {
                    Image(imageVector = Icons.Filled.Mail, contentDescription = null)
                }
            }
        }
    }
}
