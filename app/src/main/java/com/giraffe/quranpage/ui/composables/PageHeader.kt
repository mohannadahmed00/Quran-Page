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
import kotlinx.coroutines.Job

@Composable
fun PageHeader(
    modifier: Modifier = Modifier,
    surahName:String,
    juz:Int,
    onSurahNameClick: () -> Job,
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