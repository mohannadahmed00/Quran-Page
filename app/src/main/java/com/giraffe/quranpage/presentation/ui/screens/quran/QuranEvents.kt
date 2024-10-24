package com.giraffe.quranpage.presentation.ui.screens.quran

import com.giraffe.quranpage.domain.entities.ReciterEntity
import com.giraffe.quranpage.domain.entities.SurahDataEntity
import com.giraffe.quranpage.domain.entities.VerseEntity

interface QuranEvents {
    fun onAction(action: QuranScreenActions)
    fun getTafseer(
        surahIndex: Int,
        verseIndex: Int
    )

    fun selectVerseToRead(verse: VerseEntity?)
    fun selectVerse(verse: VerseEntity?)
    fun setFirstVerseOfPage(pageIndex: Int)
    fun updateReciter(reciter: ReciterEntity?)
    fun setRecentDownload(url: String, recentSurahToDownload: SurahDataEntity)
    fun clearRecentDownload()
    fun bookmarkVerse(verse: VerseEntity)
    fun removeBookmarkedVerse()
}