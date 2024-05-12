package com.giraffe.quranpage.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.hilt.navigation.compose.hiltViewModel
import com.giraffe.quranpage.ui.composables.LoadingText
import com.giraffe.quranpage.ui.composables.Pager


@Composable
fun HomeScreen(
    mViewModel: HomeViewModel = hiltViewModel()
) {
    val state by mViewModel.state.collectAsState()
    HomeScreenContent(state, mViewModel::changePageIndex)
}

@Composable
fun HomeScreenContent(state: HomeState = HomeState(), changePageIndex: (Int) -> Unit) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        if (state.pages.size == 0)
            LoadingText()
        else
            Pager(state.pages, changePageIndex)
    }
}



