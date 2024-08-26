package com.giraffe.quranpage.ui.screens.quran

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.giraffe.quranpage.ui.composables.AudioPlayerDialog
import com.giraffe.quranpage.ui.composables.Page
import com.giraffe.quranpage.ui.theme.fontFamilies
import com.giraffe.quranpage.utils.AudioPlayerManager
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
                            label = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier
                                            .size(44.dp)
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
        val context = LocalContext.current
        val isPlaying = AudioPlayerManager.isPlaying.collectAsState()
        val interactionSource = remember { MutableInteractionSource() }
        var showOptionsBottomSheet by remember { mutableStateOf(false) }
        var showTafseerBottomSheet by remember { mutableStateOf(false) }
        var showRecitersBottomSheet by remember { mutableStateOf(false) }
        var isPlayerDialogVisible by remember { mutableStateOf(false) }
        val audioPlayer = remember { AudioPlayerManager }
        LaunchedEffect(state.ayahs) {
            audioPlayer.setAyahs(state.ayahs)
        }
        LaunchedEffect(state.selectedAudioData) {
            state.selectedAudioData?.let {
                Log.d("TAG", "selectedAudioData: ${state.selectedVerseToRead} ")
                audioPlayer.initializePlayer(
                    context,
                    it,
                    state.selectedVerseToRead
                )
            }
        }
        LaunchedEffect(state.pageIndexToRead) {
            state.pageIndexToRead?.let { pagerState.scrollToPage(it - 1) }
        }
        DisposableEffect(Unit) {
            onDispose {
                audioPlayer.release()
            }
        }




        HorizontalPager(
            modifier = Modifier.clickable(
                interactionSource = interactionSource,
                indication = null
            ) { isPlayerDialogVisible = !isPlayerDialogVisible },
            state = pagerState,
            reverseLayout = true,
        ) { page ->
            Page(
                modifier = Modifier.fillMaxSize(),
                pageUI = state.pages[page],
                onVerseSelected = { verse ->
                    events.selectVerse(verse)
                    events.highlightVerse()
                    showOptionsBottomSheet = true
                },
                onSurahNameClick = { scope.launch { drawerState.open() } },
                onPartClick = {},
                onPageClick = { isPlayerDialogVisible = !isPlayerDialogVisible }
            )
        }
        if (isPlayerDialogVisible) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(vertical = 26.sdp, horizontal = 8.sdp), Arrangement.Bottom
            ) {
                AudioPlayerDialog(
                    selectedReciter = state.selectedReciter?.name ?: "",
                    surahName = "سورة ${state.surahesData[state.selectedAudioData?.surahId?.minus(1) ?: 0].arabic}",
                    audioPlayer = audioPlayer,
                    selectedVerseToRead = state.selectedVerseToRead,
                    isPlaying = isPlaying.value,
                    highlightVerse = events::highlightVerse,
                    selectVerseToRead = events::selectVerseToRead,
                ) {
                    showRecitersBottomSheet = true
                }
            }
        }
        if (showRecitersBottomSheet) {
            if (state.selectedVerseToRead == null) {
                val page = pagerState.currentPage
                Log.d("bale", "page: $page")
                val firstVerse = state.pages[pagerState.currentPage].contents[0].verses[0]
                Log.d("bale", "firstVerse: ${firstVerse.surahNumber} : ${firstVerse.verseNumber}")
                events.selectVerseToRead(firstVerse)
            }
            ModalBottomSheet(
                modifier = Modifier.fillMaxHeight(),
                sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
                onDismissRequest = { showRecitersBottomSheet = false }
            ) {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    LazyColumn(contentPadding = PaddingValues(vertical = 4.sdp)) {
                        items(state.reciters) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.sdp)
                                    .clickable {
                                        val surahAudioData =
                                            it.surahesAudioData.firstOrNull { it.surahId == (state.selectedVerseToRead?.surahNumber) }
                                        if (surahAudioData == null) {
                                            events.downloadSurahForReciter(it)
                                        } else {
                                            events.onReciterClick(it, surahAudioData)
                                        }
                                        showRecitersBottomSheet = false
                                    }
                            ) {
                                val isDownloaded = it.surahesAudioData.firstOrNull {
                                    it.surahId == (state.selectedVerseToRead?.surahNumber ?: 0)
                                } == null
                                Image(
                                    modifier = Modifier
                                        .size(25.sdp),
                                    painter = painterResource(id = if (isDownloaded) R.drawable.ic_download else R.drawable.ic_check),
                                    contentDescription = "download",
                                )
                                Spacer(modifier = Modifier.width(4.sdp))
                                Text(
                                    text = it.name,
                                    style = TextStyle(
                                        fontSize = 18.ssp,
                                    ),
                                )
                                Spacer(modifier = Modifier.width(4.sdp))
                                if (it.rewaya != "حفص عن عاصم") Text(
                                    text = "(${it.rewaya})",
                                    style = TextStyle(
                                        fontSize = 16.ssp,
                                    ),
                                )
                            }

                        }

                    }
                }
            }
        }








        if (showOptionsBottomSheet) {
            ModalBottomSheet(
                modifier = Modifier.fillMaxHeight(),
                sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false),
                onDismissRequest = {
                    showOptionsBottomSheet = false
                }
            ) {
                val surah = state.surahesData[(state.selectedVerse?.surahNumber?.minus(1)) ?: 0]
                Button(
                    modifier = Modifier.fillMaxWidth(),

                    onClick = {
                        events.getTafseer(surah.id, state.selectedVerse?.verseNumber ?: 1)
                        showTafseerBottomSheet = true
                    }) {
                    Text("Tafseer")
                }
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {}) {
                    Text("Play from here")
                }
                Button(
                    modifier = Modifier.fillMaxWidth(),
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
                    Box(
                        modifier = Modifier
                            .padding(8.sdp)
                            .fillMaxWidth()
                            .height(1.sdp)
                    )
                    state.selectedVerseTafseer?.let {
                        Text(
                            it.text,
                            style = TextStyle(
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
    }
}

