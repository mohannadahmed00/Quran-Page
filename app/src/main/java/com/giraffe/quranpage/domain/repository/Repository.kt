package com.giraffe.quranpage.domain.repository

import com.giraffe.quranpage.common.utils.domain.NetworkError
import com.giraffe.quranpage.common.utils.domain.Resource
import com.giraffe.quranpage.domain.entities.ReciterEntity
import com.giraffe.quranpage.domain.entities.SurahDataEntity
import com.giraffe.quranpage.domain.entities.TafseerEntity
import com.giraffe.quranpage.domain.entities.VerseEntity
import com.giraffe.quranpage.domain.entities.VerseTimingEntity

interface Repository {
    suspend fun bookmarkVerse(verse: VerseEntity)
    suspend fun getBookmarkedVerse(): VerseEntity?
    suspend fun removeBookmarkedVerse()
    suspend fun saveLastPageIndex(pageIndex: Int)
    suspend fun getLastPageIndex(): Int?
    fun getAllVerses(): List<VerseEntity>
    fun getSurahesData(): List<SurahDataEntity>
    suspend fun getTafseer(verse: VerseEntity): Resource<TafseerEntity, NetworkError>
    suspend fun getVersesTiming(
        surahIndex: Int,
        reciterId: Int,
    ): Resource<List<VerseTimingEntity>, NetworkError>

    suspend fun getReciters(): List<ReciterEntity>
    suspend fun getReciter(reciterId: Int): ReciterEntity
    suspend fun updateReciterData(reciter: ReciterEntity): Long
}