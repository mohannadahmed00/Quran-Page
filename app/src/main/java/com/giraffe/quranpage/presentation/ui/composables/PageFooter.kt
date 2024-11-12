package com.giraffe.quranpage.presentation.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun PageFooter(modifier: Modifier = Modifier, pageIndex: Int, hezb: String?) {
    Row(
        modifier = modifier.padding(horizontal = 10.sdp, vertical = 8.sdp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.weight(1f))
        Text(
            text = (pageIndex).toString(),
            style = TextStyle(
                fontSize = 10.ssp,
                fontWeight = FontWeight.Bold
            )
        )
        Box(modifier = Modifier.weight(1f), Alignment.CenterEnd) {
            hezb?.let {
                Text(
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.secondary,
                            shape = RoundedCornerShape(5.sdp)
                        )
                        .padding(horizontal = 4.sdp, vertical = 2.sdp),
                    text = it,
                    style = TextStyle(
                        fontSize = 10.ssp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.background
                    )
                )
            }
        }
    }
}