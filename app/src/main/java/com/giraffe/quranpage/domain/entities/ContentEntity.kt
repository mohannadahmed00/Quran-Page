package com.giraffe.quranpage.domain.entities


data class ContentEntity(
    val surahNameAr: String,
    val verses: List<VerseEntity>,
    val pageIndex: Int
)
