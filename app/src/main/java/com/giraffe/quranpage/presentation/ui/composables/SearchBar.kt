package com.giraffe.quranpage.presentation.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDirection
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun SearchBar(
    onBackClick: () -> Unit,
    onValueChange: (String) -> Unit,
) {
    var value by remember {
        mutableStateOf("")
    }
    Surface(
        shadowElevation = 10.sdp,
    ) {
        Row(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .fillMaxWidth()
                .padding(16.sdp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            OutlinedTextField(
                modifier = Modifier.weight(1f),
                textStyle = TextStyle(fontSize = 16.ssp, textDirection = TextDirection.Content),
                value = value,
                colors = OutlinedTextFieldDefaults.colors().copy(
                    focusedContainerColor = MaterialTheme.colorScheme.background,
                    unfocusedContainerColor = MaterialTheme.colorScheme.background,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                ),
                onValueChange = {
                    value = it
                    onValueChange(value)
                },
                trailingIcon = {
                    if (value.isNotEmpty()) Icon(
                        modifier = Modifier
                            .size(15.sdp)
                            .clickable {
                                value = ""
                                onValueChange(value)
                            },
                        imageVector = Icons.Default.Clear, contentDescription = "Clear"
                    )
                },
                leadingIcon = {
                    Icon(
                        modifier = Modifier
                            .size(20.sdp)
                            .clickable {
                                onBackClick()
                            },
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = "ArrowBack"
                    )
                }
            )

        }
    }
}