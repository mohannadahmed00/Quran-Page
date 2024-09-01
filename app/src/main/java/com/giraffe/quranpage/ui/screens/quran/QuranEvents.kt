package com.giraffe.quranpage.ui.screens.quran

import com.giraffe.quranpage.di.Reciters
import com.giraffe.quranpage.local.model.ReciterModel
import com.giraffe.quranpage.local.model.SurahAudioModel
import com.giraffe.quranpage.local.model.SurahModel
import com.giraffe.quranpage.local.model.VerseModel
import com.giraffe.quranpage.remote.response.ReciterResponse
import com.giraffe.quranpage.service.DownloadService.DownloadedAudio

interface QuranEvents {
    fun highlightVerse()
    fun getTafseer(
        surahIndex: Int,
        ayahIndex: Int
    )
    fun onReciterClick(reciter: ReciterModel,surahAudioData: SurahAudioModel)
    fun selectVerseToRead(verse: VerseModel?)
    fun selectVerse(verse: VerseModel?)
    fun setFirstVerse(verse: VerseModel?)
    fun clearAudioData()
    fun saveAudioFile(downloadedAudio: DownloadedAudio)
}