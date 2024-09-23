package com.giraffe.quranpage.ui.composables

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.giraffe.quranpage.local.model.VerseModel
import com.giraffe.quranpage.ui.screens.quran.PageUI
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun PageContent(
    modifier: Modifier = Modifier,
    pageUI: PageUI,
    onVerseSelected: (VerseModel) -> Unit,
    onPageClick: () -> Unit
) {
    Column(modifier = modifier, Arrangement.Center) {
        pageUI.contents.forEach { content ->
            val verse = content.verses.firstOrNull { verse -> verse.verseNumber == 1 }
            if (verse != null) {
                SurahHeader(
                    modifier = Modifier.padding(
                        bottom = if (pageUI.pageIndex == 1 || pageUI.pageIndex == 2) 8.sdp else 4.sdp,
                        top = 4.sdp
                    ),
                    surahName = content.surahNameAr
                )
                if (verse.surahNumber != 1 && verse.surahNumber != 9) Image(
                    modifier = Modifier.padding(
                        horizontal = 65.sdp
                    ), painter = painterResource(id = R.drawable.basmala), contentDescription = ""
                )
            }
            val onLongClick: (offset: Int) -> Unit = {
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
            val layoutResult = remember { mutableStateOf<TextLayoutResult?>(null) }
            val gesture = Modifier.pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = { pos ->
                        layoutResult.value?.let { layout ->
                            onLongClick(layout.getOffsetForPosition(pos))
                        }
                    },
                    onTap = {
                        onPageClick()
                    }
                )
            }
            Text(
                modifier = Modifier
                    .then(gesture)
                    .padding(horizontal = if (pageUI.pageIndex == 1 || pageUI.pageIndex == 2) 30.sdp else 0.sdp),
                text = content.text,
                onTextLayout = {
                    layoutResult.value = it
                },
                style = TextStyle(
                    fontSize = 20.ssp,
                    textDirection = TextDirection.Rtl,
                    lineHeight = 39.ssp,
                    localeList = LocaleList(Locale("ar")),
                    textAlign = TextAlign.Center,
                ),
                /*onClick = { offset ->
                    content.verses.forEach { verse ->
                        content.text.getStringAnnotations(
                            tag = verse.qcfData,
                            start = offset,
                            end = offset
                        )
                            .firstOrNull()?.let {
                                Log.d("PageContent", "Click: $($offset,$offset)")
                                //onVerseSelected(verse)
                            }
                    }

                }*/
            )
        }
    }

}