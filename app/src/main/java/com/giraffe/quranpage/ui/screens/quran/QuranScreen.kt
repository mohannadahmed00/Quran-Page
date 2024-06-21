package com.giraffe.quranpage.ui.screens.quran

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.intl.LocaleList
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.hilt.navigation.compose.hiltViewModel
import com.giraffe.quranpage.local.model.VerseModel
import com.giraffe.quranpage.utils.toArabic
import ir.kaaveh.sdpcompose.sdp
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
    HorizontalPager(
        state = pagerState,
        reverseLayout = true,
    ) { page ->
        Page(
            modifier = Modifier.fillMaxSize(),
            pageUI = state.pages[page],onVerseSelected = events::onVerseSelected)
    }

    /*val lazyListState = rememberLazyListState()
    LazyRow(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxSize(),
        state = lazyListState,
        reverseLayout = true,
        flingBehavior = rememberSnapFlingBehavior(lazyListState = lazyListState),
    ) {
        items(state.pages) { page ->
            Page(
                modifier = Modifier.fillParentMaxSize(),
                pageUI = page,onVerseSelected =events::onVerseSelected)
        }
    }*/
}

@Composable
fun Page(
    modifier: Modifier = Modifier,
    pageUI: PageUI, onVerseSelected: (VerseModel) -> Unit) {
    val context = LocalContext.current
    Column(
        modifier = modifier
            .statusBarsPadding(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ClickableText(
            text = pageUI.text,
            style = TextStyle(
                fontSize = 20.ssp,
                textDirection = TextDirection.Rtl,
                lineHeight = 40.ssp,
                localeList = LocaleList(Locale("ar")),
                textAlign = TextAlign.Center,
            ),
            onClick = { offset ->
                pageUI.verses.forEach { verse ->
                    pageUI.text.getStringAnnotations(
                        tag = verse.qcfData,
                        start = offset,
                        end = offset
                    )
                        .firstOrNull()?.let {
                            onVerseSelected(verse)
                            Log.d(
                                "TAG",
                                "(${verse.verseNumber}) => ${verse.normalContent}"
                            )
                            Toast.makeText(
                                context,
                                "${verse.normalContent} (${verse.verseNumber.toArabic()})",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }

            }
        )
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.sdp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Text(text = (pageUI.pageIndex).toArabic())
        }
    }

}