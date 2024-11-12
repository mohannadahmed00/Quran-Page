package com.giraffe.quranpage.presentation.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun PageHeader(
    modifier: Modifier = Modifier,
    surahName: String,
    juz: Int,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.sdp, vertical = 14.sdp), Arrangement.SpaceBetween
    ) {
        Text(
            text = surahName,
            style = TextStyle(
                fontSize = 10.ssp,
                fontWeight = FontWeight.Bold,
            )
        )
        Text(
            text = "juz' $juz",
            style = TextStyle(
                fontSize = 10.ssp,
                fontWeight = FontWeight.Bold,
            )
        )
    }
}