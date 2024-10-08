package com.giraffe.quranpage.ui.screens.quran

import com.giraffe.quranpage.local.model.ReciterModel
import com.giraffe.quranpage.local.model.SurahAudioModel
import com.giraffe.quranpage.local.model.VerseModel
import com.giraffe.quranpage.service.DownloadService
import com.giraffe.quranpage.service.DownloadService.DownloadedAudio

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
    fun setRecentUrl(url: String?)
    fun saveLastPageIndex(pageIndex:Int)
    fun clearRecentDownload()
}