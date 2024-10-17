package com.giraffe.quranpage.domain.entities

data class SurahDataEntity(
    val id: Int,
    val arabicName: String,
    val countOfVerses: Int,
    val endPageIndex: Int,
    val englishName: String,
    val place: String,
    val startPageIndex: Int,
    val turkishName: String,
)
