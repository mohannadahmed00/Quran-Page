package com.giraffe.quranpage.presentation.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import com.giraffe.quranpage.R
import com.giraffe.quranpage.presentation.ui.theme.hafsSmart
import ir.kaaveh.sdpcompose.ssp

@Composable
fun SurahHeader(modifier: Modifier = Modifier, surahName: String) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Image(
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)),
            painter = painterResource(id = R.drawable.surah_header),
            contentDescription = ""
        )
        Text(
            text = surahName,
            style = TextStyle(
                fontSize = 20.ssp,
                fontFamily = hafsSmart,
                color = MaterialTheme.colorScheme.primary
            )
        )
    }

}