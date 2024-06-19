package com.giraffe.quranpage.ui.screens.quran

import androidx.compose.ui.text.font.FontFamily
import com.giraffe.quranpage.local.model.VerseModel
import com.giraffe.quranpage.ui.theme.kingFahd003

data class QuranScreenState(
    val isLoading: Boolean = false,
    val content: String = "",
    val verses: List<VerseModel> = mutableListOf(),
    val versesStr: List<String> = mutableListOf(),
    val fontFamily: FontFamily = kingFahd003
)
