package com.giraffe.quranpage.ui.screens.quran

import com.giraffe.quranpage.local.model.VerseModel

interface QuranEvents {
    fun getPageContent(pageIndex: Int)
    fun onVerseSelected(verse: VerseModel)
}