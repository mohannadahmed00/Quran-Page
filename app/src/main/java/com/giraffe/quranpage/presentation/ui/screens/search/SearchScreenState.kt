package com.giraffe.quranpage.presentation.ui.screens.search

import com.giraffe.quranpage.domain.entities.SurahDataEntity
import com.giraffe.quranpage.domain.entities.VerseEntity

data class SearchScreenState(
    val value: String = "",
    val verses: List<VerseEntity> = emptyList(),
    val filteredVerses: List<VerseEntity> = emptyList(),
    val surahesData: List<SurahDataEntity> = emptyList()
)
