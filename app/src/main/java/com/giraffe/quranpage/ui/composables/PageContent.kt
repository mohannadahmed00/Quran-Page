package com.giraffe.quranpage.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.intl.LocaleList
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import com.giraffe.quranpage.R
import com.giraffe.quranpage.local.model.VerseModel
import com.giraffe.quranpage.ui.screens.quran.Content
import com.giraffe.quranpage.ui.screens.quran.PageUI
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun PageContent(
    modifier: Modifier = Modifier,
    pageUI: PageUI,
    onVerseSelected: (PageUI, Content, VerseModel) -> Unit
) {
    Column(modifier = modifier, Arrangement.Center) {
        pageUI.contents.forEach { content ->
            val verse = content.verses.firstOrNull { verse -> verse.verseNumber == 1 }
            if (verse != null) {
                SurahHeader(
                    modifier = Modifier.padding(
                        bottom = if (pageUI.pageIndex == 1 || pageUI.pageIndex == 2) 16.sdp else 8.sdp,
                        top = 8.sdp
                    ),
                    surahName = content.surahNameAr
                )
                if (verse.surahNumber != 1 && verse.surahNumber != 9) Image(
                    modifier = Modifier.padding(
                        horizontal = 65.sdp
                    ), painter = painterResource(id = R.drawable.basmala), contentDescription = ""
                )
            }
            ClickableText(
                modifier = Modifier.padding(horizontal = if (pageUI.pageIndex == 1 || pageUI.pageIndex == 2) 30.sdp else 0.sdp),
                text = content.text,
                style = TextStyle(
                    fontSize = 20.ssp,
                    textDirection = TextDirection.Rtl,
                    lineHeight = 39.ssp,
                    localeList = LocaleList(Locale("ar")),
                    textAlign = TextAlign.Center,
                ),
                onClick = { offset ->
                    content.verses.forEach { verse ->
                        content.text.getStringAnnotations(
                            tag = verse.qcfData,
                            start = offset,
                            end = offset
                        )
                            .firstOrNull()?.let {
                                onVerseSelected(pageUI, content, verse)
                            }
                    }

                }
            )
        }
    }

}