package com.giraffe.quranpage.ui.screens.quran

import com.giraffe.quranpage.local.model.VerseModel

interface QuranEvents {
    fun onVerseSelected(verse: VerseModel)
    fun onPageSelected(pageIndex: Int)
}