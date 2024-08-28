package com.giraffe.quranpage.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import com.giraffe.quranpage.R
import com.giraffe.quranpage.local.model.ReciterModel
import com.giraffe.quranpage.local.model.SurahAudioModel
import com.giraffe.quranpage.local.model.VerseModel
import com.giraffe.quranpage.ui.screens.quran.PageUI
import com.giraffe.quranpage.utils.AudioPlayerManager
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp


@Composable
fun AudioPlayerDialog(
    pages: List<PageUI>,
    currentPage: Int,
    selectedReciter: ReciterModel?,
    surahName: String,
    audioPlayer: AudioPlayerManager,
    selectedVerseToRead: VerseModel?,
    isPlaying: Boolean,
    isAudioDataExist: Boolean,
    highlightVerse: () -> Unit,
    selectVerseToRead: (VerseModel?) -> Unit,
    setFirstVerse: (VerseModel?) -> Unit,
    clearAudioData: () -> Unit,
    downloadSurahForReciter: (ReciterModel) -> Unit,
    onReciterClick: (ReciterModel, SurahAudioModel) -> Unit,
    showRecitersBottomSheet: () -> Unit,
) {
    Card {
        Column(
            modifier = Modifier.padding(4.sdp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = if (isAudioDataExist) Arrangement.SpaceBetween else Arrangement.Center
            ) {
                if (isAudioDataExist) Spacer(modifier = Modifier.width(1.sdp))
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier
                            .clickable { showRecitersBottomSheet() },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = selectedReciter?.name ?: "loading...",
                            style = TextStyle(
                                textAlign = TextAlign.Center,
                                fontSize = 16.ssp
                            ),
                        )
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "KeyboardArrowDown"
                        )
                        if (!isAudioDataExist) Image(
                            modifier = Modifier
                                .size(35.sdp)
                                .clickable {
                                    val firstVerse =
                                        pages.getOrNull(currentPage)?.contents?.getOrNull(0)?.verses?.getOrNull(
                                            0
                                        )
                                    setFirstVerse(firstVerse)
                                    val surahAudioData =
                                        selectedReciter?.surahesAudioData?.firstOrNull { surah -> surah.surahId == (firstVerse?.surahNumber) }
                                    if (surahAudioData == null) {
                                        selectedReciter?.let {
                                            downloadSurahForReciter(selectedReciter)
                                        }
                                    } else {
                                        onReciterClick(selectedReciter, surahAudioData)
                                    }
                                },
                            painter = painterResource(id = R.drawable.ic_play),
                            contentDescription = "play",
                            colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.secondary)

                        )
                    }

                    if (isAudioDataExist) Text(
                        text = surahName,
                        style = TextStyle(
                            textAlign = TextAlign.Center,
                            fontSize = 14.ssp
                        ),
                    )
                }
                if (isAudioDataExist) Icon(modifier = Modifier.clickable {
                    clearAudioData()
                    selectVerseToRead(null)
                    highlightVerse()
                    audioPlayer.release()
                }, imageVector = Icons.Default.Close, contentDescription = "close")
            }

            if (isAudioDataExist) Row(
                modifier = Modifier
                    .fillMaxWidth(),
                Arrangement.SpaceEvenly
            ) {
                Image(
                    modifier = Modifier
                        .size(35.sdp)
                        .clickable {
                            audioPlayer.seekTo(
                                selectedVerseToRead?.verseNumber?.minus(1) ?: 0
                            )
                            if (!isPlaying) audioPlayer.resume {
                                selectVerseToRead(it)
                                highlightVerse()
                            }
                        },
                    painter = painterResource(id = R.drawable.ic_previous),
                    contentDescription = "previous",
                    colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.secondary)

                )
                if (!isPlaying) Image(
                    modifier = Modifier
                        .size(35.sdp)
                        .clickable {
                            audioPlayer.play {
                                selectVerseToRead(it)
                                highlightVerse()
                            }
                        },
                    painter = painterResource(id = R.drawable.ic_play),
                    contentDescription = "play",
                    colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.secondary)

                ) else Image(
                    modifier = Modifier
                        .size(35.sdp)
                        .clickable {
                            audioPlayer.pause()
                        },
                    painter = painterResource(id = R.drawable.ic_pause),
                    contentDescription = "pause",
                    colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.secondary)

                )
                Image(
                    modifier = Modifier
                        .size(35.sdp)
                        .clickable {
                            audioPlayer.seekTo(
                                selectedVerseToRead?.verseNumber?.plus(1) ?: 1
                            )
                            if (!isPlaying) audioPlayer.resume {
                                selectVerseToRead(it)
                                highlightVerse()
                            }
                        },
                    painter = painterResource(id = R.drawable.ic_next),
                    contentDescription = "next",
                    colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.secondary)
                )
            }
        }
    }
}