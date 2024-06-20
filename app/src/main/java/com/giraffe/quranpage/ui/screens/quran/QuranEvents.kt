package com.giraffe.quranpage.ui.screens.quran

import com.giraffe.quranpage.local.model.VerseModel

interface QuranEvents {
    fun handleVerses(verses: List<VerseModel>):String
    fun handleVerse(isFirst: Boolean, verse: String): String
    fun onVerseSelected(verse: VerseModel)
    fun onPageIndexChanged(pageIndex: Int)
}