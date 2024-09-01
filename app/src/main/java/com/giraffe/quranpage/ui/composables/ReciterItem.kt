package com.giraffe.quranpage.ui.composables

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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import com.giraffe.quranpage.R
import com.giraffe.quranpage.local.model.ReciterModel
import com.giraffe.quranpage.local.model.SurahAudioModel
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun ReciterItem(
    reciter: ReciterModel? = null,
    surahAudioData: SurahAudioModel? = null,
    surahId: Int = 0,
    url: String = "",
    progress: Int = 0,
    onReciterClick: (ReciterModel, SurahAudioModel) -> Unit = { _, _ -> },
    downloadSurahForReciter: (Int, Int, String) -> Unit = { _, _, _ -> }
) {
    val isDownloaded = surahAudioData != null
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.sdp)
            .clickable {
                if (isDownloaded) {
                    onReciterClick(reciter!!, surahAudioData!!)
                } else {
                    downloadSurahForReciter(surahId, reciter?.id ?: 0, url)
                }
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(25.sdp), contentAlignment = Alignment.Center
        ) {
            if (progress == 0) {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(id = if (isDownloaded) R.drawable.ic_check else R.drawable.ic_download),
                    contentDescription = "download",
                    colorFilter = ColorFilter.tint(
                        color = if (isDownloaded) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.secondary.copy(
                            alpha = 0.4f
                        )
                    )
                )
            } else {
                CircularProgressIndicator(
                    progress = { progress.toFloat() / 100 },
                    trackColor = Color.Gray,
                )
            }


        }

        Spacer(modifier = Modifier.width(4.sdp))
        Text(
            text = reciter?.name ?: "",
            style = TextStyle(
                fontSize = 18.ssp,
            ),
        )
        Spacer(modifier = Modifier.width(4.sdp))
        if (reciter?.rewaya != "حفص عن عاصم") Text(
            text = "(${reciter?.rewaya})",
            style = TextStyle(
                fontSize = 16.ssp,
            ),
        )
    }
}


@Preview
@Composable
fun ReciterItemPreview() {
    ReciterItem()
}