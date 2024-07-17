package com.giraffe.quranpage.ui.screens.quran

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.giraffe.quranpage.ui.composables.Page

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
        //modifier = Modifier.background(background),
        state = pagerState,
        reverseLayout = true,
    ) { page ->
        Page(
            modifier = Modifier.fillMaxSize(),
            pageUI = state.pages[page],
            onVerseSelected = events::onVerseSelected,
            onPageSelected = events::onPageSelected
        )
    }
}

