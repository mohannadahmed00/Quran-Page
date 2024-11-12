package com.giraffe.quranpage.presentation.ui.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.giraffe.quranpage.R
import ir.kaaveh.sdpcompose.sdp

@Composable
fun ErrorAlertDialog(
    onDismissRequest: () -> Unit,
    dialogTitle: String,
    icon: ImageVector = Icons.Rounded.Error,
) {
    AlertDialog(
        title = {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = icon, contentDescription = "Error Icon")
                Text(
                    modifier = Modifier.padding(horizontal = 8.sdp),
                    text = stringResource(R.string.error))
            }
        },
        text = {
            Text(text = dialogTitle)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {},
        dismissButton = {
            Text(
                modifier = Modifier.clickable(onClick = onDismissRequest),
                text = stringResource(R.string.dismiss)
            )
        }
    )
}