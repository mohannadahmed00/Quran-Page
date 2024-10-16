package com.giraffe.quranpage.ui.composables

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
import com.giraffe.quranpage.local.model.ReciterModel
import com.giraffe.quranpage.local.model.SurahAudioModel
import com.giraffe.quranpage.local.model.SurahModel
import com.giraffe.quranpage.local.model.VerseModel
import com.giraffe.quranpage.utils.toThreeDigits
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp


@Composable
fun AudioPlayerDialog(
    selectedReciter: ReciterModel?,
    surahesData: List<SurahModel>,
    playerSurahAudioData: SurahAudioModel?,
    selectedVerseToRead: VerseModel?,
    isPlaying: Boolean,
    firstVerse: VerseModel?,
    isRecentDownloaded: Boolean,
    recentUrl: String?,
    recentSurahToDownload: SurahModel?,
    highlightVerse: () -> Unit,
    selectVerseToRead: (VerseModel?) -> Unit,
    cancelDownload: (String) -> Unit,
    clearRecent: () -> Unit,
    setSurahAudioData: (SurahAudioModel?) -> Unit,
    setReciter: (ReciterModel?) -> Unit,
    showRecitersBottomSheet: () -> Unit,
    pause: () -> Unit,
    play: () -> Unit,
    release: () -> Unit,
    seekTo: (verseIndex: Int) -> Unit,
    downloadSurahForReciter: (Int, ReciterModel, String, String, String) -> Unit,
) {
    val reciterSurahAudioData by remember(
        selectedReciter,
        firstVerse
    ) { derivedStateOf { selectedReciter?.surahesAudioData?.firstOrNull { surah -> surah.surahId == (firstVerse?.surahNumber) } } }
    val isPlayerAudioDataExist by remember(playerSurahAudioData) { derivedStateOf { playerSurahAudioData != null } }
    val isRecentUrlExist by remember(recentUrl) { derivedStateOf { recentUrl != null } }
    val surah by remember(surahesData, playerSurahAudioData, firstVerse) {
        derivedStateOf {
            surahesData.getOrNull(
                playerSurahAudioData?.surahId?.minus(1) ?: firstVerse?.surahNumber?.minus(1)
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
                                firstVerse.surahNumber,
                                selectedReciter,
                                selectedReciter.folderUrl + firstVerse.surahNumber.toThreeDigits() + ".mp3",
                                selectedReciter.name,
                                surah?.name ?: ""
                            )
                        }
                    }
                } else {
                    setReciter(selectedReciter)
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
                            fontSize = 16.ssp
                        ),
                    )
                    if (!isPlayerAudioDataExist && !isRecentUrlExist) {
                        Image(
                            modifier = playButtonModifier.size(35.sdp),
                            imageVector = Icons.Rounded.PlayArrow,
                            contentDescription = "play",
                            colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.secondary)

                        )
                    }
                }
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.TopEnd) {
                    if (isPlayerAudioDataExist || isRecentUrlExist) Icon(
                        modifier = Modifier.clickable {
                            if (isPlayerAudioDataExist) {
                                setSurahAudioData(null)
                                selectVerseToRead(null)
                                highlightVerse()
                                release()
                            } else {
                                cancelDownload(recentUrl ?: "")
                                clearRecent()
                            }
                        },
                        imageVector = Icons.Default.Close, contentDescription = "close"
                    )
                }
            }
            if (isPlayerAudioDataExist || (isRecentUrlExist && !isRecentDownloaded)) {
                Text(
                    text = if (isRecentUrlExist && !isRecentDownloaded) recentSurahToDownload?.arabic
                        ?: "" else surah?.arabic ?: "",
                    style = TextStyle(
                        textAlign = TextAlign.Center,
                        fontSize = 14.ssp
                    ),
                )
            }
            if (isRecentUrlExist && !isRecentDownloaded) Text(text = "Downloading...")
            if (isPlayerAudioDataExist) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    Arrangement.SpaceEvenly
                ) {
                    Image(
                        modifier = Modifier
                            .size(35.sdp)
                            .clickable { seekTo(selectedVerseToRead?.verseNumber?.minus(1) ?: 0) },
                        imageVector = Icons.Rounded.SkipPrevious,
                        contentDescription = "previous",
                        colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.secondary)

                    )
                    if (!isPlaying) Image(
                        modifier = Modifier
                            .size(35.sdp)
                            .clickable { play() },
                        imageVector = Icons.Rounded.PlayArrow,
                        contentDescription = "play",
                        colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.secondary)

                    ) else Image(
                        modifier = Modifier
                            .size(35.sdp)
                            .clickable { pause() },
                        imageVector = Icons.Rounded.Pause,
                        contentDescription = "pause",
                        colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.secondary)

                    )
                    Image(
                        modifier = Modifier
                            .size(35.sdp)
                            .clickable { seekTo(selectedVerseToRead?.verseNumber?.plus(1) ?: 1) },
                        imageVector = Icons.Rounded.SkipNext,
                        contentDescription = "next",
                        colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.secondary)
                    )
                }
            }
        }
    }
}