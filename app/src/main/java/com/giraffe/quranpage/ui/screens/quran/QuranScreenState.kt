package com.giraffe.quranpage.ui.screens.quran

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import com.giraffe.quranpage.local.model.SurahModel
import com.giraffe.quranpage.local.model.VerseModel


data class QuranScreenState(
    val selectedVerse: VerseModel? = null,
    val orgPages: List<PageUI> = listOf(),
    val pages: List<PageUI> = listOf(),
    val surahesData: List<SurahModel> = listOf(),
    val ayahs: List<VerseModel> = listOf(),
    val currentPageIndex: Int = 0
)

@Immutable
data class PageUI(
    val pageIndex: Int,
    val orgContents:List<Content>,
    val contents:List<Content>,
    val fontFamily: FontFamily,
    val surahName: String,
    val juz: Int,
    val hezb: String? = null,
    val hasSajdah: Boolean = false
)

@Immutable
data class Content(
    val surahNameAr: String,
    val verses: List<VerseModel>,
    val text: AnnotatedString
)

