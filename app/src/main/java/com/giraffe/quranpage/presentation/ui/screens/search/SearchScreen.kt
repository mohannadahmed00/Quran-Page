package com.giraffe.quranpage.presentation.ui.screens.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDirection
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.giraffe.quranpage.presentation.ui.composables.SearchBar
import com.giraffe.quranpage.presentation.ui.screens.quran.QuranArgs.Companion.SEARCH_RESULT
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel(),
    navController: NavController,
) {
    val state by viewModel.state.collectAsState()
    SearchContent(state = state, events = viewModel, navController = navController)
}

@Composable
fun SearchContent(
    state: SearchScreenState,
    events: SearchEvents,
    navController: NavController
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SearchBar(
            onBackClick = { navController.popBackStack() },
            onValueChange = { events.onValueChange(it) },
        )
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.sdp),
            contentPadding = PaddingValues(horizontal = 8.sdp, vertical = 16.sdp),
            horizontalAlignment = AbsoluteAlignment.Right
        ) {
            items(state.filteredVerses) {
                Card(
                    modifier = Modifier.clickable {
                        navController.popBackStack()
                        navController.currentBackStackEntry?.savedStateHandle?.set(
                            SEARCH_RESULT,
                            it
                        )
                    },
                    colors = CardDefaults.cardColors()
                        .copy(containerColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                ) {
                    Column(
                        modifier = Modifier.padding(8.sdp),
                    ) {
                        Text(
                            text = it.qcfContent,
                            style = TextStyle(
                                fontFamily = com.giraffe.quranpage.presentation.ui.theme.fontFamilies[(it.pageIndex) - 1],
                                textDirection = TextDirection.ContentOrRtl,
                                fontSize = 24.ssp,
                            )
                        )
                        Text(
                            text = " سورة ${state.surahesData[it.surahIndex - 1].arabicName}",
                            style = TextStyle(
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }

                }

            }
        }
    }
}