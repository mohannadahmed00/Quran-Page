package com.giraffe.quranpage.presentation.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.material.icons.rounded.SkipPrevious
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import com.giraffe.quranpage.common.utils.toThreeDigits
import com.giraffe.quranpage.domain.entities.ReciterEntity
import com.giraffe.quranpage.domain.entities.SurahAudioDataEntity
import com.giraffe.quranpage.domain.entities.SurahDataEntity
import com.giraffe.quranpage.domain.entities.VerseEntity
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp


@Composable
fun AudioPlayerDialog(
    audioPlayerSurahAudioData: SurahAudioDataEntity?,
    isPlaying: Boolean,
    selectedReciter: ReciterEntity?,
    surahesData: List<SurahDataEntity>,
    firstVerse: VerseEntity?,
    selectedVerseToRead: VerseEntity?,
    isRecentDownloaded: Boolean,
    recentUrl: String?,
    recentSurahToDownload: SurahDataEntity?,
    selectVerseToRead: (VerseEntity?) -> Unit,
    unhighlightVerse: (Boolean) -> Unit,
    selectReciter: (ReciterEntity?) -> Unit,
    clearRecentDownload: () -> Unit,
    setSurahAudioData: (SurahAudioDataEntity?) -> Unit,
    downloadSurahForReciter: (Int, ReciterEntity, String, String, String) -> Unit,
    cancelDownloadAudio: (String) -> Unit,
    showRecitersBottomSheet: () -> Unit,
    play: () -> Unit,
    pause: () -> Unit,
    seekTo: (verseIndex: Int) -> Unit,
    release: () -> Unit,
) {
    val reciterSurahAudioData by remember(
        selectedReciter,
        firstVerse
    ) { derivedStateOf { selectedReciter?.surahesAudioData?.firstOrNull { surah -> surah.surahIndex == (firstVerse?.surahIndex) } } }
    val isPlayerAudioDataExist by remember(audioPlayerSurahAudioData) { derivedStateOf { audioPlayerSurahAudioData != null } }
    val isRecentUrlExist by remember(recentUrl) { derivedStateOf { recentUrl != null } }
    val surah by remember(surahesData, audioPlayerSurahAudioData, firstVerse) {
        derivedStateOf {
            surahesData.getOrNull(
                audioPlayerSurahAudioData?.surahIndex?.minus(1) ?: firstVerse?.surahIndex?.minus(1)
                ?: 0
            )
        }
    }
    val reciterNameModifier = remember { Modifier.clickable { showRecitersBottomSheet() } }
    val playButtonModifier = remember(reciterSurahAudioData, firstVerse, selectedReciter, surah) {
        Modifier
            .clickable {
                if (reciterSurahAudioData == null) {
                    selectedReciter?.let { selectedReciter ->
                        firstVerse?.let { firstVerse ->
                            downloadSurahForReciter(
                                firstVerse.surahIndex,
                                selectedReciter,
                                selectedReciter.folderUrl + firstVerse.surahIndex.toThreeDigits() + ".mp3",
                                selectedReciter.name,
                                surah?.englishName ?: ""
                            )
                        }
                    }
                } else {
                    selectReciter(selectedReciter)
                    setSurahAudioData(reciterSurahAudioData)
                }
            }
    }
    Card(
        modifier = Modifier.padding(vertical = 26.sdp, horizontal = 8.sdp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.sdp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row {
                Box(modifier = Modifier.weight(1f))
                Row(
                    modifier = Modifier.weight(3f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        modifier = reciterNameModifier,
                        text = selectedReciter?.name ?: "Loading...",
                        style = TextStyle(
                            textAlign = TextAlign.Center,
                            fontSize = if (selectedReciter?.name != null) 16.ssp else 14.ssp
                        ),
                    )
                    if (!isPlayerAudioDataExist && !isRecentUrlExist) {
                        Image(
                            modifier = playButtonModifier.size(30.sdp),
                            imageVector = Icons.Rounded.PlayArrow,
                            contentDescription = "play",
                            colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.secondary)

                        )
                    }
                }
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.TopEnd) {
                    if (isPlayerAudioDataExist || isRecentUrlExist) Icon(
                        modifier = Modifier
                            .size(20.sdp)
                            .clickable {
                                if (isPlayerAudioDataExist) {
                                    setSurahAudioData(null)
                                    selectVerseToRead(null)
                                    unhighlightVerse(true)
                                    release()
                                } else {
                                    cancelDownloadAudio(recentUrl ?: "")
                                    clearRecentDownload()
                                }
                            },
                        imageVector = Icons.Default.Close, contentDescription = "close"
                    )
                }
            }
            if (isPlayerAudioDataExist || (isRecentUrlExist && !isRecentDownloaded)) {
                Text(
                    text = if (isRecentUrlExist && !isRecentDownloaded) recentSurahToDownload?.arabicName
                        ?: "" else surah?.arabicName ?: "",
                    style = TextStyle(
                        textAlign = TextAlign.Center,
                        fontSize = 16.ssp
                    ),
                )
            }
            if (isRecentUrlExist && !isRecentDownloaded) Text(
                text = "Downloading...",
                fontSize = 14.ssp
            )
            if (isPlayerAudioDataExist) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    Arrangement.SpaceEvenly
                ) {
                    Image(
                        modifier = Modifier
                            .size(30.sdp)
                            .clickable { seekTo(selectedVerseToRead?.verseIndex?.minus(1) ?: 0) },
                        imageVector = Icons.Rounded.SkipPrevious,
                        contentDescription = "previous",
                        colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.secondary)

                    )
                    if (!isPlaying) Image(
                        modifier = Modifier
                            .size(30.sdp)
                            .clickable { play() },
                        imageVector = Icons.Rounded.PlayArrow,
                        contentDescription = "play",
                        colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.secondary)

                    ) else Image(
                        modifier = Modifier
                            .size(30.sdp)
                            .clickable { pause() },
                        imageVector = Icons.Rounded.Pause,
                        contentDescription = "pause",
                        colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.secondary)

                    )
                    Image(
                        modifier = Modifier
                            .size(30.sdp)
                            .clickable { seekTo(selectedVerseToRead?.verseIndex?.plus(1) ?: 1) },
                        imageVector = Icons.Rounded.SkipNext,
                        contentDescription = "next",
                        colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.secondary)
                    )
                }
            }
        }
    }
}