package com.giraffe.quranpage.ui.screens.quran

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.intl.LocaleList
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.text.withStyle
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

    val text = buildAnnotatedString {
        state.verses.forEachIndexed { index, verse ->
            pushStringAnnotation(tag = verse.qcfData, annotation = verse.qcfData)
            withStyle(
                SpanStyle(
                    background = if (verse.pageIndex == state.pageIndex && verse.verseNumber == state.selectedVerse?.verseNumber) Color.Green.copy(
                        alpha = 0.1f
                    ) else Color.Transparent
                )
            ) {
                append(events.handleVerse(index == 0, verse.qcfData))
            }
            pop()
        }
    }
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(), contentAlignment = Alignment.Center
    ) {
        ClickableText(
            text = text,
            style = TextStyle(
                fontSize = 20.ssp,
                textDirection = TextDirection.Rtl,
                lineHeight = 40.ssp,
                localeList = LocaleList(Locale("ar")),
                textAlign = TextAlign.Center,
                fontFamily = state.fontFamily
            ),
            onClick = { offset ->
                state.verses.forEach { verse ->
                    text.getStringAnnotations(tag = verse.qcfData, start = offset, end = offset)
                        .firstOrNull()?.let {
                            events.onVerseSelected(verse)
                            Toast.makeText(
                                context,
                                verse.verseNumber.toString(),
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                }
            }
        )
    }
}