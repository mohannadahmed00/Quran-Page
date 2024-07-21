package com.giraffe.quranpage.remote.api

import com.giraffe.quranpage.remote.response.SurahResponse
import com.giraffe.quranpage.remote.response.QuranDataResponse
import com.giraffe.quranpage.remote.response.ReciterResponse
import com.giraffe.quranpage.remote.response.TafseerResponse
import com.giraffe.quranpage.utils.Constants.EndPoints
import com.giraffe.quranpage.utils.Constants.EndPoints.AYAT_TIMING
import com.giraffe.quranpage.utils.Constants.EndPoints.READS
import com.giraffe.quranpage.utils.Constants.QueryParameters
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RecitersApiServices {
    @GET(AYAT_TIMING)
    suspend fun getAyahsOfSurah(
        @Query(QueryParameters.SURAH) surahIndex: Int,
        @Query(QueryParameters.READ) read: Int = 5
    ): Response<SurahResponse>

    @GET("$AYAT_TIMING/$READS")
    suspend fun getReciters(): Response<List<ReciterResponse>>
}