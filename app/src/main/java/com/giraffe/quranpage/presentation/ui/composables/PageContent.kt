package com.giraffe.quranpage.presentation.ui.composables

import android.content.res.Configuration
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.intl.LocaleList
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.sp
import com.giraffe.quranpage.domain.entities.SurahDataEntity
import com.giraffe.quranpage.domain.entities.VerseEntity
import com.giraffe.quranpage.presentation.ui.screens.quran.PageUi
import com.giraffe.quranpage.presentation.ui.theme.fontFamilies
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun PageContent(
    modifier: Modifier = Modifier,
    pageUI: PageUi,
    surahesData: List<SurahDataEntity>,
    onVerseSelected: (VerseEntity) -> Unit,
    onPageClick: () -> Unit
) {
    val configuration = LocalConfiguration.current
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        pageUI.contents.forEach { content ->
            val onLongClick = remember<(offset: Int) -> Unit> {
                {
                    content.verses.forEach { verse ->
                        content.text.getStringAnnotations(
                            tag = verse.qcfContent,
                            start = it,
                            end = it
                        ).firstOrNull()?.let { onVerseSelected(verse) }
                    }
                }
            }
            val isFirstVerseOfSurahExist by remember { derivedStateOf { content.verses.first().verseIndex == 1 } }
            var layoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }
            val contentModifier = remember {
                Modifier.pointerInput(Unit) {
                    detectTapGestures(
                        onLongPress = { pos ->
                            layoutResult?.let { layout ->
                                onLongClick(
                                    layout.getOffsetForPosition(
                                        pos
                                    )
                                )
                            }
                        },
                        onTap = { onPageClick() }
                    )
                }
            }

            val fontSize = if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                if (content.verses.first().pageIndex == 575 && content.verses.first().surahIndex == 74) 42.ssp else 43.ssp
            } else {
                if (content.verses.first().pageIndex == 575 && content.verses.first().surahIndex == 74) 25.sp else 20.ssp
            }

            val basmFontSize =
                if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 40.ssp else 20.ssp

            val padding = if (pageUI.pageIndex == 1 || pageUI.pageIndex == 2){
                if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 65.sdp else 30.sdp
            }else{
                0.sdp
            }



            if (isFirstVerseOfSurahExist) {
                if (layoutResult?.lineCount != 14 || pageUI.pageIndex == 187) {
                    SurahHeader(surahData = surahesData[content.verses.first().surahIndex - 1])
                }
                if (pageUI.pageIndex != 1 && pageUI.pageIndex != 187) Text(
                    modifier = Modifier.padding(top = 4.sdp),
                    text = "ﱁ‏ﱂﱃﱄ",
                    fontFamily = fontFamilies[0],
                    fontSize = basmFontSize
                )
            }



            Text(
                modifier = contentModifier.padding(horizontal = padding),
                text = content.text,
                onTextLayout = {
                    layoutResult = it
                },
                style = TextStyle(
                    fontSize = fontSize,
                    textDirection = TextDirection.Rtl,
                    lineHeight = if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 85.ssp else 39.ssp,
                    localeList = LocaleList(Locale("ar")),
                    textAlign = TextAlign.Center,
                ),
            )
            if (!isFirstVerseOfSurahExist && layoutResult?.lineCount == 14) {
                SurahHeader(
                    surahData = surahesData.getOrNull(content.verses.first().surahIndex)
                        ?: surahesData.first()
                )
            }
        }
    }

}