package com.giraffe.quranpage.ui.screens.quran

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
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
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
    val context = LocalContext.current
    val audioPlayer = remember { AudioPlayerManager }
    val isPlaying = AudioPlayerManager.isPlaying.collectAsState()
    val interactionSource = remember { MutableInteractionSource() }
    var isOptionsBottomSheetVisible by remember { mutableStateOf(false) }
    var isTafseerBottomSheetVisible by remember { mutableStateOf(false) }
    var isRecitersBottomSheetVisible by remember { mutableStateOf(false) }
    var isPlayerDialogVisible by remember { mutableStateOf(false) }

    LaunchedEffect(state.ayahs) {
        audioPlayer.setAyahs(state.ayahs)
    }
    LaunchedEffect(state.selectedAudioData) {
        state.selectedAudioData?.let {
            audioPlayer.initializePlayer(
                context,
                it,
                state.selectedVerseToRead ?: state.firstVerse
            )
            isPlayerDialogVisible = true
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
                    isOptionsBottomSheetVisible = true
                },
                onSurahNameClick = { scope.launch { drawerState.open() } },
                onPageClick = { isPlayerDialogVisible = !isPlayerDialogVisible }
            )
        }
        if (isOptionsBottomSheetVisible) {
            ModalBottomSheet(
                modifier = Modifier.fillMaxHeight(),
                sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false),
                onDismissRequest = {
                    events.selectVerse(null)
                    events.highlightVerse()
                    isOptionsBottomSheetVisible = false
                }
            ) {
                val surah = state.surahesData[(state.selectedVerse?.surahNumber?.minus(1)) ?: 0]
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        events.getTafseer(surah.id, state.selectedVerse?.verseNumber ?: 1)
                        isTafseerBottomSheetVisible = true
                    }) {
                    Text("Tafseer")
                }
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        val tempVerse = state.selectedVerse
                        events.selectVerse(null)
                        events.highlightVerse()
                        events.selectVerseToRead(tempVerse)
                        val surahAudioData =
                            state.selectedReciter?.surahesAudioData?.firstOrNull { surah -> surah.surahId == (tempVerse?.surahNumber) }
                        if (surahAudioData == null) {
                            state.selectedReciter?.let {
                                audioPlayer.release()
                                events.downloadSurahForReciter(state.selectedReciter)
                            }
                        } else {
                            if (surahAudioData == state.selectedAudioData) {
                                audioPlayer.seekTo(tempVerse?.verseNumber ?: 0)
                            } else {
                                audioPlayer.release()
                                events.onReciterClick(state.selectedReciter, surahAudioData)
                            }
                        }
                        isOptionsBottomSheetVisible = false
                    }) {
                    Text("Play from here")
                }
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {}) {
                    Text("Bookmark")
                }

            }
        }
        if (isPlayerDialogVisible) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(vertical = 26.sdp, horizontal = 8.sdp), Arrangement.Bottom
            ) {
                AudioPlayerDialog(
                    pages = state.pages,
                    currentPage = pagerState.currentPage,
                    selectedVerseToRead = state.selectedVerseToRead,
                    selectedReciter = state.selectedReciter,
                    surahName = "سورة ${state.surahesData[state.selectedAudioData?.surahId?.minus(1) ?: 0].arabic}",
                    audioPlayer = audioPlayer,
                    isPlaying = isPlaying.value,
                    isAudioDataExist = state.selectedAudioData != null,
                    highlightVerse = events::highlightVerse,
                    selectVerseToRead = events::selectVerseToRead,
                    clearAudioData = events::clearAudioData,
                    downloadSurahForReciter = events::downloadSurahForReciter,
                    onReciterClick = events::onReciterClick,
                    setFirstVerse = events::setFirstVerse
                ) {
                    isRecitersBottomSheetVisible = true
                }
            }
        }
        if (isRecitersBottomSheetVisible) {
            ModalBottomSheet(
                modifier = Modifier.fillMaxHeight(),
                sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
                onDismissRequest = { isRecitersBottomSheetVisible = false }
            ) {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    LazyColumn(contentPadding = PaddingValues(vertical = 4.sdp)) {
                        events.setFirstVerse(
                            state.pages.getOrNull(pagerState.currentPage)?.contents?.getOrNull(
                                0
                            )?.verses?.getOrNull(0)
                        )
                        val verse = state.selectedVerseToRead ?: state.firstVerse
                        val surah = state.surahesData[verse?.surahNumber?.minus(1) ?: 0]
                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "سورة ${surah.arabic}",
                                    style = TextStyle(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.ssp
                                    )
                                )
                            }
                        }
                        item {
                            Box(
                                modifier = Modifier
                                    .padding(8.sdp)
                                    .fillMaxWidth()
                                    .height(1.sdp)
                            )
                        }
                        items(state.reciters) {
                            val surahAudioData =
                                it.surahesAudioData.firstOrNull { surahData -> surahData.surahId == surah.id }
                            val isDownloaded = surahAudioData != null
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.sdp)
                                    .clickable {
                                        if (isDownloaded) {
                                            events.onReciterClick(it, surahAudioData!!)
                                        } else {
                                            events.downloadSurahForReciter(it)
                                        }
                                        isRecitersBottomSheetVisible = false
                                    }
                            ) {
                                Image(
                                    modifier = Modifier
                                        .size(25.sdp),
                                    painter = painterResource(id = if (isDownloaded) R.drawable.ic_check else R.drawable.ic_download),
                                    contentDescription = "download",
                                    colorFilter = ColorFilter.tint(
                                        color = if (isDownloaded) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.secondary.copy(
                                            alpha = 0.4f
                                        )
                                    )
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
        if (isTafseerBottomSheetVisible) {
            ModalBottomSheet(
                modifier = Modifier.fillMaxHeight(),
                sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
                onDismissRequest = { isTafseerBottomSheetVisible = false }
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

