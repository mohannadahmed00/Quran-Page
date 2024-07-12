package com.giraffe.quranpage.ui.screens.quran

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.font.FontFamily
import com.giraffe.quranpage.local.model.SurahModel
import com.giraffe.quranpage.local.model.VerseModel


data class QuranScreenState(
    val selectedVerse: VerseModel? = null,
    val pages:List<PageUI> = listOf(),
    val surahesData:List<SurahModel> = listOf(),
    val ayahs: List<VerseModel> = listOf()
)

@Immutable
data class PageUI(
    val pageIndex:Int,
    val verses:List<VerseModel>,
    val fontFamily: FontFamily,
    val surahName: String,
    val juz: Int,
    val hezb:String? = null
)