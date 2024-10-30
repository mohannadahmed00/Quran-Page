package com.giraffe.quranpage.presentation.ui.composables

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.giraffe.quranpage.R

@Composable
fun ErrorAlertDialog(
    onDismissRequest: () -> Unit,
    dialogTitle: String,
    icon: ImageVector = Icons.Rounded.Error,
) {
    AlertDialog(
        icon = {
            Icon(
                modifier = Modifier.size(40.dp),
                imageVector = icon, contentDescription = "Error Icon")
        },
        title = {
            Text(
                text = dialogTitle,
                textAlign = TextAlign.Center
            )
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {},
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text(stringResource(R.string.dismiss))
            }
        }
    )
}