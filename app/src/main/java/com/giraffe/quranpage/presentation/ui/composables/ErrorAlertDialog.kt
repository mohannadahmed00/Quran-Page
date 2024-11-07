package com.giraffe.quranpage.presentation.ui.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.giraffe.quranpage.R

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
                HorizontalDivider(modifier = Modifier.width(8.dp))
                Text(text = stringResource(R.string.error))
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