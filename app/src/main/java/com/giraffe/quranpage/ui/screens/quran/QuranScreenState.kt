package com.giraffe.quranpage.ui.screens.quran

import androidx.compose.ui.text.font.FontFamily
import com.giraffe.quranpage.local.model.VerseModel
import com.giraffe.quranpage.ui.theme.kingFahd001


data class QuranScreenState(
    val allVerses: List<VerseModel> = mutableListOf(),
    val pageVerses: List<VerseModel> = mutableListOf(),
    val selectedVerse: VerseModel? = null,
    val pageIndex: Int = 1,
    val pageFont: FontFamily = kingFahd001,
)