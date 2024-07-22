package com.giraffe.quranpage.ui.screens.quran

import com.giraffe.quranpage.di.Reciters
import com.giraffe.quranpage.local.model.ReciterModel
import com.giraffe.quranpage.local.model.VerseModel
import com.giraffe.quranpage.remote.response.ReciterResponse

interface QuranEvents {
    fun onVerseSelected(pageUI: PageUI, content: Content, verse: VerseModel)
    fun onPageChanged(pageIndex: Int)
    fun getTafseer(
        surahIndex: Int,
        ayahIndex: Int
    )
    fun onReciterClick(reciter: ReciterModel)
    fun downloadSurahForReciter(reciter: ReciterModel)
}