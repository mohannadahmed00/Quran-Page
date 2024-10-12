package com.giraffe.quranpage.ui.screens.quran

import com.giraffe.quranpage.local.model.ReciterModel
import com.giraffe.quranpage.local.model.SurahModel
import com.giraffe.quranpage.local.model.VerseModel

interface QuranEvents {
    fun highlightVerse()
    fun getTafseer(
        surahIndex: Int,
        ayahIndex: Int
    )

    fun selectVerseToRead(verse: VerseModel?)
    fun selectVerse(verse: VerseModel?)
    fun setFirstVerse(verse: VerseModel?)
    fun updateReciter(reciter: ReciterModel?)
    fun setRecent(url: String, recentSurahToDownload: SurahModel)
    fun clearRecent()
    fun saveLastPageIndex(pageIndex: Int)
    fun clearRecentDownload()

    fun bookmarkVerse(verseModel: VerseModel?)
    fun getBookmarkedVerse()
    fun setLastPageIndex(pageIndex: Int)
}