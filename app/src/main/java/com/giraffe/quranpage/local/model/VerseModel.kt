package com.giraffe.quranpage.local.model

import com.giraffe.quranpage.remote.response.TafseerResponse


data class VerseModel(
    val content: String,
    val normalContent: String,
    val qcfData: String,
    val surahNumber: Int,
    val verseNumber: Int,
    val pageIndex: Int,
    val sajda: Boolean = false,
    val quarterHezbIndex: Int,
    val tafseer:TafseerResponse? = null
)