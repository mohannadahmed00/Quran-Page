package com.giraffe.quranpage.ui.screens.quran

import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
import com.giraffe.quranpage.local.model.ReciterModel
import com.giraffe.quranpage.service.DownloadService
import com.giraffe.quranpage.service.PlaybackService
import com.giraffe.quranpage.ui.composables.AppBar
import com.giraffe.quranpage.ui.composables.AudioPlayerDialog
import com.giraffe.quranpage.ui.composables.Page
import com.giraffe.quranpage.ui.composables.ReciterItem
import com.giraffe.quranpage.ui.theme.fontFamilies
import com.giraffe.quranpage.utils.AudioPlayerManager
import com.giraffe.quranpage.utils.Constants.Actions.CANCEL_DOWNLOAD
import com.giraffe.quranpage.utils.Constants.Actions.PLAY
import com.giraffe.quranpage.utils.Constants.Actions.START_DOWNLOAD
import com.giraffe.quranpage.utils.Constants.Keys.NOTIFICATION_ID
import com.giraffe.quranpage.utils.Constants.Keys.RECITER_ID
import com.giraffe.quranpage.utils.Constants.Keys.RECITER_NAME
import com.giraffe.quranpage.utils.Constants.Keys.SURAH_ID
import com.giraffe.quranpage.utils.Constants.Keys.SURAH_NAME
import com.giraffe.quranpage.utils.Constants.Keys.URL
import com.giraffe.quranpage.utils.ServiceConnection
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun QuranScreen(
    viewModel: QuranViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    QuranContent(state, viewModel)
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun QuranContent(
    state: QuranScreenState = QuranScreenState(),
    events: QuranEvents,
) {
    //=================================controllers=================================
    val context = LocalContext.current
    val pagerState = rememberPagerState(pageCount = { state.pages.size })
    val scope = rememberCoroutineScope()
    val systemUiController = rememberSystemUiController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val interactionSource = remember { MutableInteractionSource() }
    var isOptionsBottomSheetVisible by remember { mutableStateOf(false) }
    var isTafseerBottomSheetVisible by remember { mutableStateOf(false) }
    var isRecitersBottomSheetVisible by remember { mutableStateOf(false) }
    var isPlayerDialogVisible by remember { mutableStateOf(true) }
    val openDrawer = remember { { scope.launch { drawerState.open() } } }
    val onPageClick = remember {
        {
            isPlayerDialogVisible = !isPlayerDialogVisible
            systemUiController.isStatusBarVisible = isPlayerDialogVisible
        }
    }
    val firstVerse by remember(pagerState.currentPage) {
        derivedStateOf {
            state.pages.getOrNull(pagerState.currentPage)?.contents?.getOrNull(
                0
            )?.verses?.getOrNull(0)
        }
    }
    val verse = state.selectedVerseToRead ?: firstVerse ?: state.firstVerse


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
    val downloadSurahForReciter = remember<(Int, ReciterModel, String, String, String) -> Unit> {
        { surahId, reciter, url, reciterName, surahName ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (!queue.containsKey(url)) {
                    Log.d("QuranContent", "downloadSurahForReciter($surahId, ${reciter.id}, $url)")
                    events.setRecentUrl(url)
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
                    Log.d("QuranContent", "cancelDownloadAudio($url)")
                    val intent = Intent(context, DownloadService::class.java).apply {
                        action = CANCEL_DOWNLOAD
                        putExtra(URL, url)
                    }
                    context.startForegroundService(intent)
                }
            }
        }
    }
    LaunchedEffect(playbackService) {
        playbackService?.let {
            Log.d("QuranContent", "LaunchedEffect(playbackService): $playbackService")
            if (audioPlayer.ayahs.value.isEmpty()) {
                audioPlayer.setAyahs(state.ayahs)
            }
            it.setTracker { verse ->
                events.selectVerseToRead(verse)
                events.highlightVerse()
            }
        }

    }
    LaunchedEffect(state.pages) {
        Log.d(
            "QuranContent",
            "LaunchedEffect(state.pages): currentPage = ${pagerState.currentPage}"
        )
        events.setFirstVerse(
            state.pages.getOrNull(pagerState.currentPage)?.contents?.getOrNull(
                0
            )?.verses?.getOrNull(0)
        )
    }
    LaunchedEffect(state.firstVerse) {
        Log.d("QuranContent", "LaunchedEffect(state.firstVerse): ${state.firstVerse}")
        if (!isPlaying) {
            audioPlayer.setReciter(state.reciters.firstOrNull { reciter ->
                reciter.surahesAudioData.firstOrNull { item -> item.surahId == verse?.surahNumber } != null
            })
        }
    }
    LaunchedEffect(downloadedFiles.size) {
        downloadedFiles.forEach { downloadedAudio ->
            events.updateReciter(downloadedAudio.value.reciter)
            if (downloadedAudio.value.url == state.recentUrl) {
                events.clearRecentDownload()
                audioPlayer.setReciter(downloadedAudio.value.reciter)
                audioPlayer.setSurahAudioData(downloadedAudio.value.surahAudioModel)
            }
        }
    }
    LaunchedEffect(isPrepared) {
        Log.d(
            "QuranContent",
            "LaunchedEffect(isPrepared): isPrepared = ($isPrepared), isPlaying = ($isPlaying)"
        )
        if (isPrepared && !isPlaying) {
            context.startForegroundService(playbackServiceIntent.setAction(PLAY))
        }
    }
    LaunchedEffect(surahAudioData) {
        Log.d("QuranContent", "LaunchedEffect(state.selectedAudioData): ${state.selectedAudioData}")

        surahAudioData?.let {
            if (!isPlaying) {
                val surahName = state.surahesData[verse?.surahNumber?.minus(1) ?: 0].name
                audioPlayer.initializePlayer(
                    context,
                    it,
                    verse,
                    surahName,
                    reciter?.name ?: "",
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
        Log.d("QuranContent", "DisposableEffect(Unit): Unit")
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
            Log.d("QuranContent", "DisposableEffect(Unit): onDispose")

            context.unbindService(downloadServiceConnection)
            context.unbindService(playbackServiceConnection)
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
            val pageData = remember { state.orgPages[page] }
            Page(
                pageUI = state.pages[page],
                pageData = pageData,
                onVerseSelected = { verse ->
                    events.selectVerse(verse)
                    events.highlightVerse()
                    isOptionsBottomSheetVisible = true
                },
                onPageClick = onPageClick
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
                        /*val tempVerse = state.selectedVerse
                        events.selectVerse(null)
                        events.highlightVerse()
                        events.selectVerseToRead(tempVerse)
                        val surahAudioData =
                            reciter?.surahesAudioData?.firstOrNull { surah -> surah.surahId == (tempVerse?.surahNumber) }
                        if (surahAudioData == null) {
                            state.selectedReciter?.let {
                                audioPlayer.release()
                                val surahId = state.selectedVerse?.surahNumber?:state.firstVerse?.surahNumber?:0
                                downloadSurahForReciter(context,queue,state.selectedReciter.id,state.selectedReciter.folderUrl,surahId,service)
                            }
                        } else {
                            if (surahAudioData == state.selectedAudioData) {
                                audioPlayer.seekTo(tempVerse?.verseNumber ?: 0)
                            } else {
                                audioPlayer.release()
                                events.onReciterClick(state.selectedReciter, surahAudioData)
                            }
                        }
                        isOptionsBottomSheetVisible = false*/
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
        if (isPlayerDialogVisible && state.surahesData.isNotEmpty()) {
            val surah = state.surahesData[verse?.surahNumber?.minus(1) ?: 0]
            Column(
                modifier = Modifier
                    .statusBarsPadding()
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                AppBar(
                    onMenuClick = openDrawer,
                    onSearchClick = {}
                )

                AudioPlayerDialog(
                    pages = state.pages,
                    currentPage = pagerState.currentPage,
                    selectedVerseToRead = state.selectedVerseToRead,
                    selectedReciter = reciter,
                    recentUrl = state.recentUrl,
                    surahName = "سورة ${surah.arabic}",
                    isPlaying = isPlaying,
                    isRecentDownloaded = state.isRecentDownloaded,
                    isAudioDataExist = surahAudioData != null,
                    highlightVerse = events::highlightVerse,
                    selectVerseToRead = events::selectVerseToRead,
                    //clearAudioData = audioPlayer::clearAudioData,
                    //onReciterClick = events::onReciterClick,
                    setFirstVerse = events::setFirstVerse,
                    cancelDownload = cancelDownloadAudio,
                    setSurahAudioData = audioPlayer::setSurahAudioData,
                    setReciter = audioPlayer::setReciter,
                    setRecentUrl = events::setRecentUrl,
                    showRecitersBottomSheet = { isRecitersBottomSheetVisible = true },
                    pause = { playbackService?.pause() },
                    play = { playbackService?.play() },
                    release = { playbackService?.release() },
                    seekTo = { playbackService?.seekTo(it) },
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
                        items(state.reciters) {
                            ReciterItem(
                                reciter = it,
                                surah = surah,
                                queue = queue,
                                setReciter = audioPlayer::setReciter,
                                setSurahAudioData = audioPlayer::setSurahAudioData,
                                downloadSurahForReciter = downloadSurahForReciter,
                                cancelDownloadAudio = cancelDownloadAudio,
                                setRecentUrl = events::setRecentUrl,
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
