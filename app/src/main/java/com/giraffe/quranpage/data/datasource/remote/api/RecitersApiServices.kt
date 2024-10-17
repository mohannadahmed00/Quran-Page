package com.giraffe.quranpage.data.datasource.remote.api

import com.giraffe.quranpage.data.datasource.remote.responses.ReciterResponse
import com.giraffe.quranpage.common.utils.Constants.EndPoints.AYAT_TIMING
import com.giraffe.quranpage.common.utils.Constants.EndPoints.READS
import com.giraffe.quranpage.common.utils.Constants.QueryParameters
import com.giraffe.quranpage.data.datasource.remote.responses.VerseTimingResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RecitersApiServices {
    @GET(AYAT_TIMING)
    suspend fun getVerseTimingOfSurah(
        @Query(QueryParameters.SURAH) surahIndex: Int,
        @Query(QueryParameters.READ) reciterId: Int
    ): Response<List<VerseTimingResponse>>

    @GET("$AYAT_TIMING/$READS")
    suspend fun getReciters(): Response<List<ReciterResponse>>
}