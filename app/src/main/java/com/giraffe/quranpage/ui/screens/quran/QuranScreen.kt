package com.giraffe.quranpage.ui.screens.quran

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.intl.LocaleList
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.giraffe.quranpage.R
import com.giraffe.quranpage.local.model.VerseModel
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
    val pagerState = rememberPagerState(pageCount = { state.pages.size })
    LaunchedEffect(state.selectedPageIndex) {
        pagerState.scrollToPage(state.selectedPageIndex)
    }
    HorizontalPager(
        state = pagerState,
        reverseLayout = true,
        //key = { state.pages[it].pageIndex }
    ) { page ->

        Page(
            modifier = Modifier.fillMaxSize(),
            text = convertVerseToText(
                state.pages[page].verses,
                state.pages[page].fontFamily,
                state.selectedVerse
            ),
            pageUI = state.pages[page],
            onVerseSelected = events::onVerseSelected,
            onPageSelected = events::onPageSelected
        )
    }
}

@Composable
fun Page(
    modifier: Modifier = Modifier,
    text: AnnotatedString,
    pageUI: PageUI,
    onVerseSelected: (VerseModel) -> Unit,
    onPageSelected: (Int) -> Unit
) {
    Column(
        modifier = modifier
            .statusBarsPadding(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp), Arrangement.SpaceBetween
        ) {
            Text(
                text = pageUI.surahName,
                style = TextStyle(
                    fontSize = 10.ssp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            )
            Text(
                text = "juz' ${pageUI.juz}",
                style = TextStyle(
                    fontSize = 10.ssp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        ClickableText(
            text = text,
            style = TextStyle(
                fontSize = 20.ssp,
                textDirection = TextDirection.Rtl,
                lineHeight = 40.ssp,
                localeList = LocaleList(Locale("ar")),
                textAlign = TextAlign.Center,
            ),
            onClick = { offset ->
                pageUI.verses.forEach { verse ->
                    text.getStringAnnotations(
                        tag = verse.qcfData,
                        start = offset,
                        end = offset
                    )
                        .firstOrNull()?.let {
                            onVerseSelected(verse)
                        }
                }

            }
        )
        Spacer(modifier = Modifier.weight(1f))
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier
                .weight(1f)
                .clickable {
                    onPageSelected(602 - 1)
                }) {
                if (pageUI.hasSajdah) Image(
                    modifier = Modifier.size(20.dp),
                    painter = painterResource(id = R.drawable.sajdah),
                    contentDescription = ""
                )
            }
            Text(
                text = (pageUI.pageIndex).toString(),
                style = TextStyle(
                    fontSize = 10.ssp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            )
            Box(modifier = Modifier.weight(1f), Alignment.CenterEnd) {
                pageUI.hezb?.let {
                    Text(
                        text = it,
                        style = TextStyle(
                            fontSize = 10.ssp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    )
                }
            }
        }


    }

}

fun convertVerseToText(
    verses: List<VerseModel>,
    fontFamily: FontFamily,
    selectedVerse: VerseModel? = null
): AnnotatedString {
    return buildAnnotatedString {
        verses.forEach { verse ->
            pushStringAnnotation(tag = verse.qcfData, annotation = verse.qcfData)
            withStyle(
                style = SpanStyle(
                    fontFamily = fontFamily,
                    background = if (verse.verseNumber == selectedVerse?.verseNumber && verse.pageIndex == selectedVerse.pageIndex) Color.Green.copy(
                        alpha = 0.1f
                    ) else Color.Transparent
                )
            ) {
                append(verse.qcfData)
            }
            pop()
        }
    }
}

