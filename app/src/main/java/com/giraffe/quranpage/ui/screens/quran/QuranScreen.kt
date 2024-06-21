package com.giraffe.quranpage.ui.screens.quran

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.intl.LocaleList
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.text.withStyle
import androidx.hilt.navigation.compose.hiltViewModel
import com.giraffe.quranpage.utils.toArabic
import ir.kaaveh.sdpcompose.ssp

@Composable
fun QuranScreen(
    viewModel: QuranViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    QuranContent(state, viewModel)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun QuranContent(
    state: QuranScreenState = QuranScreenState(),
    events: QuranEvents
) {

    val pagerState = rememberPagerState(pageCount = { 10 })
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            events.getPageContent(page + 1)
        }
    }
    HorizontalPager(state = pagerState, reverseLayout = true) { page ->
        val text = buildAnnotatedString {
            state.pageVerses.forEach { verse ->
                pushStringAnnotation(tag = verse.qcfData, annotation = verse.qcfData)
                withStyle(
                    SpanStyle(
                        background = if (state.selectedVerse?.pageIndex == verse.pageIndex && state.selectedVerse.verseNumber == verse.verseNumber) Color.Green.copy(
                            alpha = 0.1f
                        ) else Color.Transparent
                    )
                ) {
                    append(verse.qcfData)
                }
                pop()
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1f))
            ClickableText(
                text = text,
                style = TextStyle(
                    fontSize = 20.ssp,
                    textDirection = TextDirection.Rtl,
                    lineHeight = 40.ssp,
                    localeList = LocaleList(Locale("ar")),
                    textAlign = TextAlign.Center,
                    fontFamily = state.pageFont
                ),
                onClick = { offset ->
                    state.pageVerses.forEach { verse ->
                        text.getStringAnnotations(
                            tag = verse.qcfData,
                            start = offset,
                            end = offset
                        )
                            .firstOrNull()?.let {
                                events.onVerseSelected(verse)
                            }
                    }

                }
            )
            Spacer(modifier = Modifier.weight(1f))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Text(text = (page + 1).toArabic())
            }
        }
    }
}