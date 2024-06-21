package com.giraffe.quranpage.local.model

import androidx.compose.runtime.Immutable

@Immutable
data class VerseModel(
    val content: String,
    val normalContent: String,
    val qcfData: String,
    val surahNumber: Int,
    val verseNumber: Int,
    val pageIndex: Int,
)