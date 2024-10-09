package com.giraffe.quranpage.ui.screens.search

import com.giraffe.quranpage.local.model.SurahModel
import com.giraffe.quranpage.local.model.VerseModel

data class SearchScreenState(
    val value: String = "",
    val verses:List<VerseModel> = emptyList(),
    val filteredVerses:List<VerseModel> = emptyList(),
    val surahesData:List<SurahModel> = emptyList()
)
