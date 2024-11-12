package com.giraffe.quranpage.data.datasource.local.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class VerseModel(
    val content: String,
    val normalContent: String,
    val qcfData: String,
    val surahNumber: Int,
    val verseNumber: Int,
    val pageIndex: Int,
    val sajda: Boolean = false,
    val quarterHezbIndex: Int,
    val juz:Int,
    val surahNameAr:String,
    val surahNameEn:String,
    val lineStart:Int,
    val lineEnd:Int
) : Parcelable