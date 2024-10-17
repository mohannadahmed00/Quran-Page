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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.giraffe.quranpage.presentation.ui.composables.SearchBar
import com.giraffe.quranpage.presentation.ui.screens.quran.QuranArgs.Companion.SEARCH_RESULT

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
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 16.dp),
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
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text(

                            text = it.qcfContent,
                            style = TextStyle(
                                fontFamily = com.giraffe.quranpage.presentation.ui.theme.fontFamilies[(it.pageIndex) - 1],
                                textDirection = TextDirection.ContentOrRtl,
                                fontSize = 24.sp,
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