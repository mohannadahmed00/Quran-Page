package com.giraffe.quranpage.data.datasource.local

import com.giraffe.quranpage.data.datasource.local.models.ReciterModel
import com.giraffe.quranpage.data.datasource.local.models.SurahDataModel
import com.giraffe.quranpage.data.datasource.local.models.VerseModel

interface LocalDataSource {
    suspend fun bookmarkVerse(verseModel: VerseModel)
    suspend fun getBookmarkedVerse(): VerseModel?
    suspend fun removeBookmarkedVerse()
    suspend fun saveLastPageIndex(pageIndex: Int)
    suspend fun getLastPageIndex(): Int?
    fun storeReciter(reciterModel: ReciterModel): Long
    fun getAllReciters(): List<ReciterModel>
    fun getReciter(reciterId: Int): ReciterModel
    fun getAllVerses(): List<VerseModel>
    fun getSurahesData(): List<SurahDataModel>
    fun getRecitersCount(): Int
}