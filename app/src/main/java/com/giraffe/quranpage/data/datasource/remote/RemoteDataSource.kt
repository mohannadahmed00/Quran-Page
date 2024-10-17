package com.giraffe.quranpage.data.datasource.remote

import com.giraffe.quranpage.data.datasource.remote.responses.ReciterResponse
import com.giraffe.quranpage.data.datasource.remote.responses.TafseerResponse
import com.giraffe.quranpage.data.datasource.remote.responses.VerseTimingResponse
import retrofit2.Response

interface RemoteDataSource {
    suspend fun getVersesTimingOfSurah(
        surahIndex: Int,
        reciterId: Int = 5
    ): Response<List<VerseTimingResponse>>

    suspend fun getTafseer(
        surahIndex: Int,
        ayahIndex: Int
    ): Response<TafseerResponse>

    suspend fun getReciters(): Response<List<ReciterResponse>>
}