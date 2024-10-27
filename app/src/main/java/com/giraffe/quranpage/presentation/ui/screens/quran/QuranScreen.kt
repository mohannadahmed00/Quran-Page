package com.giraffe.quranpage.presentation.ui.screens.quran

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.giraffe.quranpage.common.service.DownloadService
import com.giraffe.quranpage.common.service.PlaybackService
import com.giraffe.quranpage.common.service.ServiceConnection
import com.giraffe.quranpage.common.utils.AudioPlayerManager
import com.giraffe.quranpage.common.utils.Constants.Actions.CANCEL_DOWNLOAD
import com.giraffe.quranpage.common.utils.Constants.Actions.PLAY
import com.giraffe.quranpage.common.utils.Constants.Actions.START_DOWNLOAD
import com.giraffe.quranpage.common.utils.Constants.Keys.NOTIFICATION_ID
import com.giraffe.quranpage.common.utils.Constants.Keys.RECITER_ID
import com.giraffe.quranpage.common.utils.Constants.Keys.RECITER_NAME
import com.giraffe.quranpage.common.utils.Constants.Keys.SURAH_ID
import com.giraffe.quranpage.common.utils.Constants.Keys.SURAH_NAME
import com.giraffe.quranpage.common.utils.Constants.Keys.URL
import com.giraffe.quranpage.common.utils.toString
import com.giraffe.quranpage.common.utils.toThreeDigits
import com.giraffe.quranpage.domain.entities.ReciterEntity
import com.giraffe.quranpage.navigateToSearch
import com.giraffe.quranpage.presentation.ui.composables.AppBar
import com.giraffe.quranpage.presentation.ui.composables.AudioPlayerDialog
import com.giraffe.quranpage.presentation.ui.composables.DrawerPartHeader
import com.giraffe.quranpage.presentation.ui.composables.Page
import com.giraffe.quranpage.presentation.ui.composables.ReciterItem
import com.giraffe.quranpage.presentation.ui.composables.SurahDrawerItem
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun QuranScreen(
    viewModel: QuranViewModel = hiltViewModel(),
    navController: NavController,
) {
    val state by viewModel.state.collectAsState()
    QuranContent(state, viewModel, navController)

}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuranContent(
    state: QuranScreenState = QuranScreenState(),
    events: QuranEvents,
    navController: NavController,
) {
    //=================================controllers=================================
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val pagerState = rememberPagerState(pageCount = { state.allPages.size })
    val scope = rememberCoroutineScope()
    val systemUiController = rememberSystemUiController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val interactionSource = remember { MutableInteractionSource() }
    var isOptionsBottomSheetVisible by remember { mutableStateOf(false) }
    var isTafseerBottomSheetVisible by remember { mutableStateOf(false) }
    var isRecitersBottomSheetVisible by remember { mutableStateOf(false) }
    var isPlayerDialogVisible by remember { mutableStateOf(true) }
    val openDrawer = remember {
        {
            scope.launch {
                drawerState.open()
            }
        }
    }
    val args = remember { QuranArgs(navController.currentBackStackEntry?.savedStateHandle) }
    val selectedSurah by remember(state.surahesData, state.selectedVerse) {
        derivedStateOf {
            state.surahesData[(state.selectedVerse?.surahIndex?.minus(1)) ?: 0]
        }
    }


    //=================================playback=================================
    val playbackServiceIntent = remember { Intent(context, PlaybackService::class.java) }
    val playbackServiceConnection =
        remember { ServiceConnection { (it as PlaybackService.LocalBinder).getService() } }
    val playbackService by playbackServiceConnection.service.collectAsState()
    val audioPlayer by playbackService?.audioPlayer?.collectAsState() ?: remember {
        mutableStateOf(AudioPlayerManager)
    }
    val isPlaying by audioPlayer.isPlaying.collectAsState()
    val isPrepared by audioPlayer.isPrepared.collectAsState()
    val surahAudioData by audioPlayer.surahAudioData.collectAsState()
    val reciter by audioPlayer.reciter.collectAsState()


    //=================================download=================================
    val downloadServiceIntent = remember { Intent(context, DownloadService::class.java) }
    val downloadServiceConnection =
        remember { ServiceConnection { (it as DownloadService.LocalBinder).getService() } }
    val downloadService by downloadServiceConnection.service.collectAsState()
    val downloadedFiles =
        remember(downloadService?.downloadedFiles) { downloadService?.downloadedFiles ?: mapOf() }
    val queue by downloadService?.queueState?.collectAsState() ?: remember {
        mutableStateOf(emptyMap())
    }
    val downloadSurahForReciter =
        remember<(Int, ReciterEntity, String, String, String) -> Unit>(state.surahesData) {
            { surahId, reciter, url, reciterName, surahName ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    if (!queue.containsKey(url)) {
                        events.setRecentDownload(url, state.surahesData[surahId - 1])
                        audioPlayer.setReciter(reciter)
                        val intent = Intent(context, DownloadService::class.java).apply {
                            action = START_DOWNLOAD
                            putExtra(NOTIFICATION_ID, url.hashCode())
                            putExtra(RECITER_ID, reciter.id)
                            putExtra(RECITER_NAME, reciterName)
                            putExtra(SURAH_ID, surahId)
                            putExtra(SURAH_NAME, surahName)
                            putExtra(URL, url)
                        }
                        context.startForegroundService(intent)
                        audioPlayer.clearAudioData()
                        audioPlayer.release()
                    }
                }
            }
        }
    val cancelDownloadAudio = remember<(String) -> Unit> {
        { url ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (!queue.containsKey(url)) {
                    val intent = Intent(context, DownloadService::class.java).apply {
                        action = CANCEL_DOWNLOAD
                        putExtra(URL, url)
                    }
                    context.startForegroundService(intent)
                }
            }
        }
    }


    LaunchedEffect(state.lastPageIndex) {
        args.searchResult?.let { searchResult ->
            events.highlightVerse(verse = searchResult)
            pagerState.scrollToPage(searchResult.pageIndex - 1)
            CoroutineScope(Dispatchers.IO).launch {
                delay(1000L)
                events.unhighlightVerse()
            }
            args.clear()
        } ?: pagerState.scrollToPage(state.lastPageIndex - 1)
    }
    LaunchedEffect(pagerState, state.allPages, state.reciters) {
        snapshotFlow { pagerState.currentPage }.collect { pageIndex ->
            events.setFirstVerseOfPage(pageIndex)
            //drawerListState.scrollToItem(getJuz(firstVerse?.pageIndex?:0))
            if (reciter == null) {
                audioPlayer.setReciter(state.reciters.firstOrNull { reciter ->
                    reciter.surahesAudioData.firstOrNull { item -> item.surahIndex == state.firstVerse?.surahIndex } != null
                } ?: state.reciters.getOrNull(0))
            }
        }
    }
    LaunchedEffect(isPlayerDialogVisible) {
        systemUiController.isStatusBarVisible = isPlayerDialogVisible
    }
    LaunchedEffect(playbackService) {
        playbackService?.let {
            it.setTracker { trackedVerse ->
                trackedVerse?.let { verse ->
                    events.highlightVerse(verse = verse, isToRead = true)
                }

            }
        }

    }
    LaunchedEffect(downloadedFiles.size) {
        downloadedFiles.forEach { downloadedAudio ->
            events.updateReciter(downloadedAudio.value.reciter)
            if (downloadedAudio.value.url == state.recentUrl) {
                events.clearRecentDownload()
                audioPlayer.setReciter(downloadedAudio.value.reciter)
                audioPlayer.setSurahAudioData(downloadedAudio.value.surahAudioDataModel)
            }
        }
    }
    LaunchedEffect(isPrepared) {
        if (isPrepared && !isPlaying) {
            context.startForegroundService(playbackServiceIntent.setAction(PLAY))
        }
    }
    LaunchedEffect(surahAudioData) {
        if (!isPlaying) {
            surahAudioData?.let { surahAudioData ->
                audioPlayer.initializePlayer(
                    context = context,
                    surahAudioData = surahAudioData,
                    currentVerse = state.selectedVerseToRead ?: state.firstVerse,
                    surahName = state.surahesData[surahAudioData.surahIndex - 1].englishName,
                    reciterName = reciter?.name ?: "",
                )
                isPlayerDialogVisible = true
            }
        }
    }
    LaunchedEffect(state.pageIndexToRead) {
        state.pageIndexToRead?.let {
            pagerState.scrollToPage(it - 1)
        }
    }
    DisposableEffect(Unit) {
        systemUiController.isStatusBarVisible = isPlayerDialogVisible
        context.bindService(
            downloadServiceIntent,
            downloadServiceConnection,
            Context.BIND_AUTO_CREATE
        )
        context.bindService(
            playbackServiceIntent,
            playbackServiceConnection,
            Context.BIND_AUTO_CREATE
        )
        onDispose {
            context.unbindService(downloadServiceConnection)
            context.unbindService(playbackServiceConnection)
        }
    }
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_STOP) {
                events.saveLastPageIndex()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    state.surahesByJuz.forEach { (juz, surahes) ->
                        DrawerPartHeader(
                            state.surahesByJuz.keys,
                            juz
                        ) {
                            scope.launch {
                                pagerState.scrollToPage(it - 1)
                                drawerState.close()
                            }
                        }
                        surahes.forEach {
                            val isSelected by remember { derivedStateOf { pagerState.currentPage >= it.startPageIndex - 1 && pagerState.currentPage <= it.endPageIndex - 1 } }
                            SurahDrawerItem(it, isSelected) {
                                scope.launch {
                                    pagerState.scrollToPage(it - 1)
                                    drawerState.close()
                                }
                            }
                        }
                    }
                }
            }
        }
    ) {
        HorizontalPager(
            modifier = Modifier.clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                isPlayerDialogVisible = !isPlayerDialogVisible
            },
            state = pagerState,
            reverseLayout = true,
        ) { page ->
            Page(
                pageUI = state.allPages[page],
                surahesData = state.surahesData,
                onVerseSelected = { verse ->
                    events.highlightVerse(verse)
                    isOptionsBottomSheetVisible = true
                },
                onPageClick = { isPlayerDialogVisible = !isPlayerDialogVisible }
            )
        }
        if (isOptionsBottomSheetVisible) {
            ModalBottomSheet(
                modifier = Modifier.fillMaxHeight(),
                sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false),
                onDismissRequest = {
                    events.unhighlightVerse()
                    isOptionsBottomSheetVisible = false
                }
            ) {

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        events.getTafseer(selectedSurah.id, state.selectedVerse?.verseIndex ?: 1)
                        isTafseerBottomSheetVisible = true
                    }) {
                    Text("Tafseer")
                }
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        events.selectVerseToRead(state.selectedVerse)
                        val reciterSurahAudioData =
                            reciter?.surahesAudioData?.firstOrNull { surah -> surah.surahIndex == (state.selectedVerse?.surahIndex) }
                        if (reciterSurahAudioData == null) {
                            reciter?.let { selectedReciter ->
                                state.selectedVerse?.let { selectedVerseToRead ->
                                    downloadSurahForReciter(
                                        selectedVerseToRead.surahIndex,
                                        selectedReciter,
                                        selectedReciter.folderUrl + selectedVerseToRead.surahIndex.toThreeDigits() + ".mp3",
                                        selectedReciter.name,
                                        selectedSurah.englishName
                                    )
                                }
                            }
                        } else {
                            audioPlayer.setReciter(reciter)
                            audioPlayer.setSurahAudioData(reciterSurahAudioData)
                        }

                    }) {
                    Text("Play from here")
                }
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        if (state.selectedVerse?.qcfContent == state.bookmarkedVerse?.qcfContent) {
                            state.selectedVerse?.let { events.removeBookmarkedVerse() }
                        } else {
                            state.selectedVerse?.let { events.bookmarkVerse(it) }
                        }
                    }) {
                    Text(if (state.selectedVerse?.qcfContent == state.bookmarkedVerse?.qcfContent) "Remove Bookmark" else "Bookmark")
                }
            }
        }
        if (isPlayerDialogVisible) {
            Column(
                modifier = Modifier
                    .statusBarsPadding()
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                AppBar(
                    bookmarkedVerse = state.bookmarkedVerse,
                    onMenuClick = openDrawer,
                    onSearchClick = { navController.navigateToSearch() },
                    onBookmarkClick = {
                        state.bookmarkedVerse?.let { verse ->
                            scope.launch {

                                pagerState.scrollToPage(
                                    verse.pageIndex - 1
                                )
                                events.highlightVerse(verse = verse)
                                CoroutineScope(Dispatchers.IO).launch {
                                    delay(1000L)
                                    events.unhighlightVerse()
                                }
                            }
                        }
                    }
                )
                AudioPlayerDialog(
                    selectedReciter = reciter,
                    selectedVerseToRead = state.selectedVerseToRead,
                    recentUrl = state.recentUrl,
                    recentSurahToDownload = state.recentSurahToDownload,
                    surahesData = state.surahesData,
                    playerSurahAudioData = surahAudioData,
                    isPlaying = isPlaying,
                    isRecentDownloaded = state.isRecentDownloaded,
                    unhighlightVerse = events::unhighlightVerse,
                    selectVerseToRead = events::selectVerseToRead,
                    firstVerse = state.firstVerse,
                    setSurahAudioData = audioPlayer::setSurahAudioData,
                    setReciter = audioPlayer::setReciter,
                    clearRecent = events::clearRecentDownload,
                    showRecitersBottomSheet = { isRecitersBottomSheetVisible = true },
                    pause = { playbackService?.pause() },
                    play = { playbackService?.play() },
                    release = { playbackService?.release() },
                    seekTo = { playbackService?.seekTo(it) },
                    downloadSurahForReciter = downloadSurahForReciter,
                    cancelDownload = cancelDownloadAudio,
                )
            }
        }
        if (isRecitersBottomSheetVisible) {
            ModalBottomSheet(
                modifier = Modifier.fillMaxHeight(),
                sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
                onDismissRequest = { isRecitersBottomSheetVisible = false }
            ) {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    LazyColumn(
                        contentPadding = PaddingValues(vertical = 4.sdp)
                    ) {
                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = state.surahesData[surahAudioData?.surahIndex?.minus(1)
                                        ?: state.firstVerse?.surahIndex?.minus(1)
                                        ?: 0].arabicName,
                                    style = TextStyle(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.ssp
                                    )
                                )
                            }
                        }
                        item {
                            HorizontalDivider(
                                modifier = Modifier.padding(
                                    horizontal = 8.dp,
                                    vertical = 16.dp
                                ), thickness = 1.dp
                            )
                        }
                        items(state.reciters) {
                            ReciterItem(
                                reciter = it,
                                surah = state.surahesData[surahAudioData?.surahIndex?.minus(1)
                                    ?: state.firstVerse?.surahIndex?.minus(1)
                                    ?: 0],
                                queue = queue,
                                setReciter = audioPlayer::setReciter,
                                setSurahAudioData = audioPlayer::setSurahAudioData,
                                downloadSurahForReciter = downloadSurahForReciter,
                                cancelDownloadAudio = cancelDownloadAudio,
                                clearRecent = events::clearRecentDownload,
                            )
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
                        state.selectedVerse?.qcfContent ?: "",
                        style = TextStyle(
                            textAlign = TextAlign.Center,
                            textDirection = TextDirection.ContentOrRtl,
                            fontFamily = com.giraffe.quranpage.presentation.ui.theme.fontFamilies[(state.selectedVerse?.pageIndex
                                ?: 1) - 1],
                            fontSize = 20.ssp
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(
                            horizontal = 8.dp,
                            vertical = 16.dp
                        ), thickness = 1.dp
                    )
                    state.selectedVerseTafseerError?.let {
                        Text(
                            it.toString(context),
                            style = TextStyle(
                                textAlign = TextAlign.Center,
                                textDirection = TextDirection.ContentOrRtl,
                                fontSize = 16.ssp
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        )
                    } ?: state.selectedVerseTafseer?.let {
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
                            "[ ${it.name} ]",
                            style = TextStyle(
                                textAlign = TextAlign.Center,
                                textDirection = TextDirection.ContentOrRtl,
                                fontSize = 16.ssp,
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
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