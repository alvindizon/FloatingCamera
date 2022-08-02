package com.alvindizon.floatingcamera.utils

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import com.alvindizon.floatingcamera.R
import java.io.File


object IntentUtils {
    fun shareFile(context: Context, file: File) {
        val uri = FileProvider.getUriForFile(context, getFileProviderAuthority(context), file)
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            setDataAndType(uri, "image/png")
            putExtra(Intent.EXTRA_STREAM, uri)
        }
        context.startActivity(
            Intent.createChooser(
                shareIntent,
                context.getString(R.string.app_name)
            )
        )
    }

    fun emailFile(context: Context, file: File) {
        val attachment =
            FileProvider.getUriForFile(context, getFileProviderAuthority(context), file)
        val emailIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.send_email_msg))
            putExtra(Intent.EXTRA_STREAM, attachment)
        }
        context.startActivity(
            Intent.createChooser(
                emailIntent,
                context.getString(R.string.send_email_msg)
            )
        )
    }
}
