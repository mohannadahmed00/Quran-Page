package com.giraffe.quranpage.presentation.ui.composables

import androidx.compose.foundation.Image
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.intl.LocaleList
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.sp
import com.giraffe.quranpage.R
import com.giraffe.quranpage.domain.entities.SurahDataEntity
import com.giraffe.quranpage.domain.entities.VerseEntity
import com.giraffe.quranpage.presentation.ui.screens.quran.PageUi
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
    Column(modifier = modifier, Arrangement.Center) {
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
            val isFirstVerseOfSurahExist by remember { derivedStateOf { content.verses[0].verseIndex == 1 } }
            val surahOfContentIndex by remember { derivedStateOf { content.verses[0].surahIndex } }
            var layoutResult by remember { mutableStateOf<TextLayoutResult?>( null) }
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
            if (isFirstVerseOfSurahExist) {
                if (layoutResult?.lineCount != 14 || surahOfContentIndex == 9) {
                    SurahHeader(
                        modifier = Modifier.padding(
                            bottom = if (pageUI.pageIndex == 1 || pageUI.pageIndex == 2) 8.sdp else 4.sdp,
                            top = 4.sdp
                        ),
                        surahName = content.surahNameAr
                    )
                }
                if (surahOfContentIndex != 1 && surahOfContentIndex != 9) Image(
                    modifier = Modifier.padding(
                        horizontal = 65.sdp
                    ),
                    painter = painterResource(id = R.drawable.basmala),
                    contentDescription = ""
                )
            }
            Text(
                modifier = contentModifier.padding(horizontal = if (pageUI.pageIndex == 1 || pageUI.pageIndex == 2) 30.sdp else 0.sdp),
                text = content.text,
                onTextLayout = {
                    layoutResult = it
                },
                style = TextStyle(
                    fontSize = if (content.verses.first().surahIndex == 74) 25.sp else 20.ssp,
                    textDirection = TextDirection.Rtl,
                    lineHeight = 39.ssp,
                    localeList = LocaleList(Locale("ar")),
                    textAlign = TextAlign.Center,
                ),
            )
            if (!isFirstVerseOfSurahExist && layoutResult?.lineCount == 14) {
                SurahHeader(
                    modifier = Modifier.padding(
                        bottom = 4.sdp,
                        top = 4.sdp
                    ),
                    surahName = surahesData.getOrNull(content.verses[0].surahIndex)?.arabicName
                        ?: ""
                )
            }
        }
    }

}