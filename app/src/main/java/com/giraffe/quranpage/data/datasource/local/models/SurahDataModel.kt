package com.giraffe.quranpage.data.datasource.local.models

data class SurahDataModel(
    val id: Int,
    val arabic: String,
    val aya: Int,
    val endPage: Int,
    val name: String,
    val place: String,
    val startPage: Int,
    val turkish: String,
)