package com.giraffe.quranpage.ui.screens.quran

import com.giraffe.quranpage.local.model.VerseModel

interface QuranEvents {
    fun onVerseSelected(pageUI: PageUI, content: Content, verse: VerseModel)
    fun onPageChanged(pageIndex: Int)
    fun getTafseer(
        surahIndex: Int,
        ayahNumber: Int
    )
}