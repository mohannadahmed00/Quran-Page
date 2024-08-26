package com.giraffe.quranpage.ui.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.giraffe.quranpage.ui.screens.quran.PageUI
import ir.kaaveh.sdpcompose.ssp

@Composable
fun PageHeader(
    modifier: Modifier = Modifier,
    pageUI: PageUI,
    onSurahNameClick: () -> Unit,
    onPartClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp), Arrangement.SpaceBetween
    ) {
        Text(
            modifier = Modifier.clickable {
                onSurahNameClick()
            },
            text = pageUI.surahName,
            style = TextStyle(
                fontSize = 10.ssp,
                fontWeight = FontWeight.Bold,
            )
        )
        Text(
            modifier = Modifier.clickable {
                onPartClick()
            },
            text = "juz' ${pageUI.juz}",
            style = TextStyle(
                fontSize = 10.ssp,
                fontWeight = FontWeight.Bold,
            )
        )
    }
}