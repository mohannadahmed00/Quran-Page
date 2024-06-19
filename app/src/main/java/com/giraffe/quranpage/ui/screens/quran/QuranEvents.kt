package com.giraffe.quranpage.ui.screens.quran

import com.giraffe.quranpage.local.model.VerseModel

interface QuranEvents {
    fun handleVerses(verses: List<VerseModel>):String
}