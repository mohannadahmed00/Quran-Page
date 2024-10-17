package com.giraffe.quranpage.ui.composables

import android.util.Log
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
import com.giraffe.quranpage.R
import com.giraffe.quranpage.local.model.SurahModel
import com.giraffe.quranpage.local.model.VerseModel
import com.giraffe.quranpage.ui.screens.quran.PageUI
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun PageContent(
    modifier: Modifier = Modifier,
    pageUI: PageUI,
    surahesData: List<SurahModel>,
    onVerseSelected: (VerseModel) -> Unit,
    onPageClick: () -> Unit
) {
    Column(modifier = modifier, Arrangement.Center) {
        pageUI.contents.forEach { content ->
            val onLongClick = remember<(offset: Int) -> Unit> {
                {
                    Log.d("PageContent", "LongClick: $($it,$it)")
                    content.verses.forEach { verse ->
                        content.text.getStringAnnotations(
                            tag = verse.qcfData,
                            start = it,
                            end = it
                        )
                            .firstOrNull()?.let {
                                onVerseSelected(verse)
                            }
                    }
                }
            }
            val firstVerseOfSurah by remember { derivedStateOf { content.verses.firstOrNull { verse -> verse.verseNumber == 1 } } }
            var layoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }
            val gesture = remember {
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
            firstVerseOfSurah?.let {
                if (layoutResult?.lineCount != 14 || it.surahNumber == 9) {
                    SurahHeader(
                        modifier = Modifier.padding(
                            bottom = if (pageUI.pageIndex == 1 || pageUI.pageIndex == 2) 8.sdp else 4.sdp,
                            top = 4.sdp
                        ),
                        surahName = content.surahNameAr
                    )
                }
                if (it.surahNumber != 1 && it.surahNumber != 9) Image(
                    modifier = Modifier.padding(
                        horizontal = 65.sdp
                    ),
                    painter = painterResource(id = R.drawable.basmala),
                    contentDescription = ""
                )
            }
            Text(
                modifier = gesture.padding(horizontal = if (pageUI.pageIndex == 1 || pageUI.pageIndex == 2) 30.sdp else 0.sdp),
                text = content.text,
                onTextLayout = {
                    layoutResult = it
                },
                style = TextStyle(
                    fontSize = 20.ssp,
                    textDirection = TextDirection.Rtl,
                    lineHeight = 39.ssp,
                    localeList = LocaleList(Locale("ar")),
                    textAlign = TextAlign.Center,
                ),
            )
            if (firstVerseOfSurah == null && layoutResult?.lineCount == 14) {
                SurahHeader(
                    modifier = Modifier.padding(
                        bottom = if (pageUI.pageIndex == 1 || pageUI.pageIndex == 2) 8.sdp else 4.sdp,
                        top = 4.sdp
                    ),
                    surahName = surahesData.getOrNull(content.verses[0].surahNumber)?.arabic ?: ""
                )
            }
        }
    }

}