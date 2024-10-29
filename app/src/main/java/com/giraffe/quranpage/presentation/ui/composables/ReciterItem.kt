package com.giraffe.quranpage.presentation.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.DownloadForOffline
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.TextStyle
import com.giraffe.quranpage.common.service.DownloadService.DownloadedAudio
import com.giraffe.quranpage.common.utils.toThreeDigits
import com.giraffe.quranpage.domain.entities.ReciterEntity
import com.giraffe.quranpage.domain.entities.SurahAudioDataEntity
import com.giraffe.quranpage.domain.entities.SurahDataEntity
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun ReciterItem(
    reciter: ReciterEntity,
    surah: SurahDataEntity,
    queue: Map<String, DownloadedAudio>,
    setReciter: (ReciterEntity) -> Unit,
    clearRecentDownload: () -> Unit,
    setSurahAudioData: (SurahAudioDataEntity) -> Unit,
    downloadSurahForReciter: (Int, ReciterEntity, String, String, String) -> Unit,
    cancelDownloadAudio: (String) -> Unit,
    completedColor: Color = MaterialTheme.colorScheme.secondary
) {
    val surahAudioData by remember(reciter) { derivedStateOf { reciter.surahesAudioData.firstOrNull { surahData -> surahData.surahIndex == surah.id } } }
    var isDownloaded by remember { mutableStateOf(surahAudioData != null) }
    val url by remember { derivedStateOf { reciter.folderUrl + surah.id.toThreeDigits() + ".mp3" } }
    val progress = queue[url]?.progress?.collectAsState()
    val imgVector by remember(progress?.value, isDownloaded) {
        derivedStateOf {
            if (progress?.value == 100 || isDownloaded) {
                isDownloaded = true
                Icons.Rounded.CheckCircle
            } else {
                Icons.Rounded.DownloadForOffline
            }
        }
    }
    val imgColor by remember(progress?.value, isDownloaded) {
        derivedStateOf {
            if (progress?.value == 100 || isDownloaded) {
                completedColor
            } else {
                completedColor.copy(alpha = 0.4f)
            }
        }
    }
    val itemModifier = remember {
        Modifier
            .fillMaxWidth()
            .clickable {
                if (isDownloaded) {
                    setReciter(reciter)
                    setSurahAudioData(surahAudioData!!)
                } else {
                    downloadSurahForReciter(surah.id, reciter, url, reciter.name, surah.englishName)
                }
            }
    }
    val cancelImgModifier = remember {
        Modifier
            .clickable {
                cancelDownloadAudio(url)
                clearRecentDownload()
            }
    }
    Row(
        modifier = itemModifier.padding(horizontal = 16.sdp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(25.sdp), contentAlignment = Alignment.Center
        ) {
            if (progress?.value == 0 || progress?.value == 100 || progress == null) {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    imageVector = imgVector,
                    contentDescription = "download",
                    colorFilter = ColorFilter.tint(color = imgColor)
                )
            } else {
                CircularProgressIndicator(
                    progress = { progress.value.toFloat() / 100 },
                    trackColor = Color.Gray,
                )
                Image(
                    modifier = cancelImgModifier,
                    imageVector = Icons.Default.Clear,
                    contentDescription = "cancel",
                    colorFilter = ColorFilter.tint(color = imgColor)
                )
            }
        }
        Spacer(modifier = Modifier.width(4.sdp))
        Text(
            text = reciter.name,
            style = TextStyle(
                fontSize = 18.ssp,
            ),
        )
        Spacer(modifier = Modifier.width(4.sdp))
        if (reciter.rewaya != "حفص عن عاصم") Text(
            text = "(${reciter.rewaya})",
            style = TextStyle(
                fontSize = 16.ssp,
            ),
        )
    }
}