package com.giraffe.quranpage.ui.screens.quran

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.intl.LocaleList
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.hilt.navigation.compose.hiltViewModel
import ir.kaaveh.sdpcompose.ssp

@Composable
fun QuranScreen(
    viewModel: QuranViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    QuranContent(state, viewModel)
}

@Composable
fun QuranContent(
    state: QuranScreenState = QuranScreenState(),
    events: QuranEvents
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(), contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.background(color = Color.Green.copy(alpha = 0.1f)),
            text = state.content,
            style = TextStyle(
                fontSize = 20.ssp,
                textDirection = TextDirection.Rtl,
                lineHeight = 40.ssp,
                localeList = LocaleList(Locale("ar")),
                textAlign = TextAlign.Center
            ),
            fontFamily = state.fontFamily
        )
    }

}