package com.giraffe.quranpage.ui.screens.quran

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import com.giraffe.quranpage.local.model.VerseModel
import com.giraffe.quranpage.ui.theme.kingFahd001


data class QuranScreenState(
    val selectedVerse: VerseModel? = null,
    val pages:List<PageUI> = listOf(),
)

data class PageUI(
    val pageIndex:Int,
    val verses:List<VerseModel>,
    val text: AnnotatedString
)