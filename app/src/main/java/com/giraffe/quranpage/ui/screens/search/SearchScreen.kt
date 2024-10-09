package com.giraffe.quranpage.ui.screens.search

import android.util.Log
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.giraffe.quranpage.ui.composables.SearchBar
import com.giraffe.quranpage.ui.theme.fontFamilies

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
                        Log.d("SearchContent", "SearchContent(verse): ${it.content}")
                        navController.popBackStack()
                        navController.currentBackStackEntry
                            ?.savedStateHandle
                            ?.set("verse", it)

                    },
                    colors = CardDefaults.cardColors()
                        .copy(containerColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                ) {
                    Column(
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text(

                            text = it.qcfData,
                            style = TextStyle(
                                fontFamily = fontFamilies[(it.pageIndex) - 1],
                                textDirection = TextDirection.ContentOrRtl,
                                fontSize = 25.sp,
                            )
                        )
                        Text(
                            text = " سورة ${state.surahesData[it.surahNumber - 1].arabic}",
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