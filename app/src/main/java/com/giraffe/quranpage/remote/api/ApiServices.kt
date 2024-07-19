package com.giraffe.quranpage.remote.api

import com.giraffe.quranpage.remote.response.SurahResponse
import com.giraffe.quranpage.remote.response.QuranDataResponse
import com.giraffe.quranpage.remote.response.TafseerResponse
import com.giraffe.quranpage.utils.Constants.EndPoints
import com.giraffe.quranpage.utils.Constants.QueryParameters
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiServices {
    @GET(EndPoints.AYAT_TIMING)
    suspend fun getAyahsOfSurah(
        @Query(QueryParameters.SURAH) surahIndex: Int,
        @Query(QueryParameters.READ) read: Int = 5
    ): Response<SurahResponse>

    @GET(EndPoints.SUWAR)
    suspend fun getQuranData(): Response<QuranDataResponse>

    @GET("${EndPoints.TAFSIR}/{tafseer_id}/{sura_index}/{ayah_index}")
    suspend fun getTafseer(
        @Path("tafseer_id") tafseerId: Int = 1,
        @Path("sura_index") surahIndex: Int,
        @Path("ayah_index") ayahNumber: Int
    ): Response<TafseerResponse>
}