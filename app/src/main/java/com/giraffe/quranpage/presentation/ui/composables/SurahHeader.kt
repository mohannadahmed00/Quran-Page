package com.giraffe.quranpage.presentation.ui.composables

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.giraffe.quranpage.R
import com.giraffe.quranpage.common.utils.presentation.getSurahNameFontFamily
import com.giraffe.quranpage.domain.entities.SurahDataEntity
import com.giraffe.quranpage.presentation.ui.theme.mcs0
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun SurahHeader(modifier: Modifier = Modifier, surahData: SurahDataEntity) {
    val configuration = LocalConfiguration.current

    val title = buildAnnotatedString {
        withStyle(
            style = SpanStyle(
                fontSize = if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 55.ssp else 25.ssp,
                fontFamily = getSurahNameFontFamily(surahData.mcsFile),
                color = MaterialTheme.colorScheme.primary,
                letterSpacing = if (surahData.id == 101) (-83).ssp else (-10).ssp
            )
        ) {
            append(surahData.mcs)
            withStyle(
                style = SpanStyle(fontFamily = mcs0)
            ) {
                append("S")
            }
        }
    }
    Box(
        modifier = modifier
            .height(if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 80.sdp else 44.sdp)
            .paint(
                painter = painterResource(id = R.drawable.surah_header),
                contentScale = ContentScale.FillBounds,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(text = title)
    }
}