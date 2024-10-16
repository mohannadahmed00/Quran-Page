package com.giraffe.quranpage.ui.composables

import android.util.Log
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import com.giraffe.quranpage.R
import com.giraffe.quranpage.local.model.ReciterModel
import com.giraffe.quranpage.local.model.SurahAudioModel
import com.giraffe.quranpage.local.model.SurahModel
import com.giraffe.quranpage.service.DownloadService.DownloadedAudio
import com.giraffe.quranpage.utils.toThreeDigits
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun ReciterItem(
    reciter: ReciterModel,
    surah: SurahModel,
    queue: Map<String, DownloadedAudio>,
    setSurahAudioData: (SurahAudioModel) -> Unit,
    setReciter: (ReciterModel) -> Unit,
    downloadSurahForReciter: (Int, ReciterModel, String, String, String) -> Unit,
    cancelDownloadAudio: (String) -> Unit,
    clearRecent: () -> Unit,
) {
    val surahAudioData =
        reciter.surahesAudioData.firstOrNull { surahData -> surahData.surahId == surah.id }
    val isDownloaded = surahAudioData != null
    val url = reciter.folderUrl + surah.id.toThreeDigits() + ".mp3"
    val progress = queue[url]?.progress?.collectAsState()
    val imgRes = remember { mutableIntStateOf(R.drawable.ic_download) }
    val color = MaterialTheme.colorScheme.secondary.copy(
        alpha = 0.4f
    )
    val rememberedColor = remember { mutableStateOf(color) }
    if (progress?.value == 100 || isDownloaded) {
        imgRes.intValue = R.drawable.ic_check
        rememberedColor.value = MaterialTheme.colorScheme.secondary
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.sdp)
            .clickable {
                if (isDownloaded) {
                    setReciter(reciter)
                    setSurahAudioData(surahAudioData!!)
                } else {
                    downloadSurahForReciter(surah.id, reciter, url, reciter.name, surah.name)
                }
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(25.sdp), contentAlignment = Alignment.Center
        ) {
            Log.d("ReciterItem", "ReciterItem(id = ${reciter.name}): ${progress?.value}")
            if (progress?.value == 0 || progress?.value == 100 || progress == null) {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(id = imgRes.intValue),
                    contentDescription = "download",
                    colorFilter = ColorFilter.tint(color = rememberedColor.value)
                )
            } else {
                CircularProgressIndicator(
                    progress = { progress.value.toFloat() / 100 },
                    trackColor = Color.Gray,
                )
                Image(
                    modifier = Modifier
                        .clickable {
                            Log.d("messi", "cancel $url")
                            cancelDownloadAudio(url)
                            clearRecent()
                        },
                    imageVector = Icons.Default.Clear,
                    contentDescription = "cancel",
                    colorFilter = ColorFilter.tint(color = rememberedColor.value)
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