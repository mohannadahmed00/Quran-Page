package com.giraffe.quranpage.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.giraffe.quranpage.R
import com.giraffe.quranpage.local.model.VerseModel
import com.giraffe.quranpage.ui.theme.backgroundLight
import com.giraffe.quranpage.ui.theme.inversePrimaryLight
import com.giraffe.quranpage.ui.theme.onSurfaceLight
import com.giraffe.quranpage.ui.theme.onTertiaryLight
import com.giraffe.quranpage.ui.theme.surfaceLight
import com.giraffe.quranpage.ui.theme.tertiaryLight
import com.giraffe.quranpage.utils.AudioPlayerManager
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp


@Composable
fun AudioPlayerDialog(
    selectedReciter: String,
    surahName: String,
    audioPlayer: AudioPlayerManager,
    selectedVerseToRead: VerseModel?,
    isPlaying: Boolean,
    highlightVerse: (Boolean) -> Unit,
    selectVerseToRead: (VerseModel?) -> Unit,
    showRecitersBottomSheet: () -> Unit,
) {
    Card {
        Column(
            modifier = Modifier.padding(4.sdp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                Spacer(modifier = Modifier.width(1.sdp))
                Column (verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
                    Text(
                        modifier = Modifier
                            .clickable { showRecitersBottomSheet() },
                        text = selectedReciter,
                        style = TextStyle(
                            textAlign = TextAlign.Center,
                            fontSize = 16.ssp
                        ),
                    )
                    Text(
                        modifier = Modifier
                            .clickable { showRecitersBottomSheet() },
                        text = surahName,
                        style = TextStyle(
                            textAlign = TextAlign.Center,
                            fontSize = 14.ssp
                        ),
                    )
                }
                Icon(modifier = Modifier.clickable {}, imageVector = Icons.Default.Close, contentDescription = "close")
            }

            Row(
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
                                highlightVerse(true)
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
                                highlightVerse(true)
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
                                highlightVerse(true)
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