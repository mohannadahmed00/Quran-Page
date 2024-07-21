package com.giraffe.quranpage.ui.screens.quran

import android.media.MediaPlayer
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.giraffe.quranpage.R
import com.giraffe.quranpage.ui.composables.Page
import com.giraffe.quranpage.ui.theme.brown
import com.giraffe.quranpage.ui.theme.cream
import com.giraffe.quranpage.ui.theme.fontFamilies
import com.giraffe.quranpage.ui.theme.transparent
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp
import kotlinx.coroutines.launch

@Composable
fun QuranScreen(
    viewModel: QuranViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    QuranContent(state, viewModel)
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun QuranContent(
    state: QuranScreenState = QuranScreenState(),
    events: QuranEvents,
) {
    val pagerState = rememberPagerState(pageCount = { state.pages.size })
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
                                                color = if (pagerState.currentPage >= it.startPage - 1 && pagerState.currentPage <= it.endPage - 1) cream else transparent,
                                                shape = CircleShape
                                            )
                                    ) {
                                        Text(text = it.id.toString())
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(text = it.name)
                                }
                            },
                            selected = pagerState.currentPage >= it.startPage - 1 && pagerState.currentPage <= it.endPage - 1,
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
        var showOptionsBottomSheet by remember { mutableStateOf(false) }
        var showTafseerBottomSheet by remember { mutableStateOf(false) }
        var showRecitersBottomSheet by remember { mutableStateOf(false) }

        val context = LocalContext.current
        val mediaPlayer = remember { MediaPlayer.create(context, R.raw.ahmed_nu_018) }
        val mediaPlayerState = remember { mutableStateOf(mediaPlayer.isPlaying) }


        HorizontalPager(
            state = pagerState,
            reverseLayout = true,
        ) { page ->
            Page(
                modifier = Modifier.fillMaxSize(),
                pageUI = state.pages[page],
                onVerseSelected = { pageUi, content, verse ->
                    events.onVerseSelected(pageUi, content, verse)
                    showOptionsBottomSheet = true
                },
                onSurahNameClick = { scope.launch { drawerState.open() } },
                onPartClick = {}
            )
        }
        if (showOptionsBottomSheet) {
            ModalBottomSheet(
                modifier = Modifier.fillMaxHeight(),
                sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false),
                onDismissRequest = { showOptionsBottomSheet = false }
            ) {
                val surah = state.surahesData[(state.selectedVerse?.surahNumber?.minus(1)) ?: 0]
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .clickable { showRecitersBottomSheet = true },
                    text = state.selectedReciter?.name ?: "",
                    style = TextStyle(
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 16.ssp
                    ),
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    Arrangement.SpaceEvenly
                ) {
                    Image(
                        modifier = Modifier.size(35.sdp),
                        painter = painterResource(id = R.drawable.ic_previous),
                        contentDescription = "previous",
                        colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onPrimary)
                    )
                    if (!mediaPlayerState.value) Image(
                        modifier = Modifier
                            .size(35.sdp)
                            .clickable {
                                mediaPlayerState.value = true
                                mediaPlayer.start()
                            },
                        painter = painterResource(id = R.drawable.ic_play),
                        contentDescription = "play",
                        colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onPrimary)
                    )
                    else Image(
                        modifier = Modifier
                            .size(35.sdp)
                            .clickable {
                                mediaPlayerState.value = false
                                mediaPlayer.pause()
                            },
                        painter = painterResource(id = R.drawable.ic_pause),
                        contentDescription = "pause",
                        colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onPrimary)
                    )
                    Image(
                        modifier = Modifier.size(35.sdp),
                        painter = painterResource(id = R.drawable.ic_next),
                        contentDescription = "next",
                        colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onPrimary)
                    )
                }
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = brown.copy(alpha = 0.1f)),
                    onClick = {
                        events.getTafseer(surah.id, state.selectedVerse?.verseNumber ?: 1)
                        showTafseerBottomSheet = true
                    }) {
                    Text("Tafseer")
                }
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = brown.copy(alpha = 0.2f)),
                    onClick = {}) {
                    Text("Share")
                }
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = brown.copy(alpha = 0.4f)),
                    onClick = {}) {
                    Text("Bookmark")
                }

            }
        }
        if (showTafseerBottomSheet) {
            ModalBottomSheet(
                modifier = Modifier.fillMaxHeight(),
                sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
                onDismissRequest = { showTafseerBottomSheet = false }
            ) {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    Text(
                        state.selectedVerse?.qcfData ?: "",
                        style = TextStyle(
                            textAlign = TextAlign.Center,
                            textDirection = TextDirection.ContentOrRtl,
                            fontFamily = fontFamilies[(state.selectedVerse?.pageIndex ?: 1) - 1],
                            fontSize = 20.ssp
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    )
                    state.selectedVerse?.tafseer?.let {
                        Text(
                            it.text,
                            style = TextStyle(
                                color = brown,
                                textAlign = TextAlign.Center,
                                textDirection = TextDirection.ContentOrRtl,
                                fontSize = 16.ssp
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        )
                        Text(
                            "[ ${it.tafseer_name} ]",
                            style = TextStyle(
                                color = brown.copy(alpha = 0.5f),
                                textAlign = TextAlign.Center,
                                textDirection = TextDirection.ContentOrRtl,
                                fontSize = 16.ssp
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        )
                    }
                }


            }
        }
        if (showRecitersBottomSheet) {
            ModalBottomSheet(
                modifier = Modifier.fillMaxHeight(),
                sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
                onDismissRequest = { showRecitersBottomSheet = false }
            ) {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl ) {
                    LazyColumn(contentPadding = PaddingValues(vertical = 4.sdp)) {
                        items(state.reciters) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.sdp)
                                    .clickable {
                                        events.onReciterClick(it)
                                        showRecitersBottomSheet = false
                                    }
                            ) {
                                Image(
                                    modifier = Modifier
                                        .size(25.sdp)
                                        .clickable {},
                                    painter = painterResource(id = R.drawable.ic_download),
                                    contentDescription = "download",
                                    colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.3f))
                                )
                                Spacer(modifier = Modifier.width(4.sdp))
                                Text(
                                    text = it.name,
                                    style = TextStyle(
                                        fontSize = 18.ssp,
                                        color = MaterialTheme.colorScheme.onPrimary
                                    ),
                                )
                                Spacer(modifier = Modifier.width(4.sdp))
                                if (it.rewaya != "حفص عن عاصم") Text(
                                    text = "(${it.rewaya})",
                                    style = TextStyle(
                                        fontSize = 16.ssp,
                                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.3f)
                                    ),
                                )
                            }

                        }

                    }
                }



            }
        }
    }
}

