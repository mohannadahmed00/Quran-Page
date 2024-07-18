package com.giraffe.quranpage.ui.screens.quran

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.giraffe.quranpage.ui.composables.Page
import com.giraffe.quranpage.ui.theme.brown
import com.giraffe.quranpage.ui.theme.cream
import com.giraffe.quranpage.ui.theme.transparent
import kotlinx.coroutines.launch

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
    events: QuranEvents,
) {
    val pagerState = rememberPagerState(pageCount = { state.pages.size })
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }
            .collect { page ->
                events.onPageChanged(page)
            }
    }
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(modifier = Modifier.height(16.dp))
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    state.surahesData.forEach {
                        NavigationDrawerItem(
                            modifier = Modifier.padding(vertical = 3.dp),
                            colors = NavigationDrawerItemDefaults.colors(
                                selectedContainerColor = brown.copy(alpha = 0.3f),
                                selectedTextColor = brown,
                                unselectedTextColor = brown,
                            ),
                            label = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier
                                            .size(44.dp)
                                            .background(
                                                color = if (state.currentPageIndex >= it.startPage - 1 && state.currentPageIndex <= it.endPage - 1) cream else transparent,
                                                shape = CircleShape
                                            )
                                    ) {
                                        Text(text = it.id.toString())
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(text = it.name)
                                }
                            },
                            selected = state.currentPageIndex >= it.startPage - 1 && state.currentPageIndex <= it.endPage - 1,
                            onClick = {
                                scope.launch {
                                    drawerState.close()
                                    pagerState.scrollToPage(it.startPage - 1)
                                }
                            },
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

            }
        }
    ) {
        HorizontalPager(
            state = pagerState,
            reverseLayout = true,
        ) { page ->
            Page(
                modifier = Modifier.fillMaxSize(),
                pageUI = state.pages[page],
                onVerseSelected = events::onVerseSelected,
                onDrawerClicked = { scope.launch { drawerState.open() } }
            )
        }
    }
}

