package com.giraffe.quranpage.local.model

data class SurahModel(
    val arabic: String,
    val aya: Int,
    val endPage: Int,
    val id: Int,
    val name: String,
    val place: String,
    val startPage: Int,
    val turkish: String,
)