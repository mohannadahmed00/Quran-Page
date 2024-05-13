package com.giraffe.quranpage.remote.api

import com.giraffe.quranpage.remote.response.SurahResponse
import com.giraffe.quranpage.remote.response.QuranDataResponse
import com.giraffe.quranpage.utils.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServices {
    @GET(Constants.EndPoints.AYAT_TIMING)
    suspend fun getAyahsOfSurah(
        @Query(Constants.QueryParameters.SURAH) surahIndex: Int,
        @Query(Constants.QueryParameters.READ) read: Int = 5
    ): Response<SurahResponse>

    @GET(Constants.EndPoints.SUWAR)
    suspend fun getQuranData(): Response<QuranDataResponse>
}