package com.giraffe.quranpage.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class VerseEntity(
    val content: String,
    val normalContent: String,
    val qcfContent: String,
    val surahIndex: Int,
    val verseIndex: Int,
    val pageIndex: Int,
    val quarterHezbIndex: Int,
    val hasSajda: Boolean = false,
    val juz:Int,
    val surahNameAr:String,
    val surahNameEn:String,
    val lineStart:Int,
    val lineEnd:Int
) : Parcelable
