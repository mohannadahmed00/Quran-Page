package com.giraffe.quranpage.remote.api

import com.giraffe.quranpage.remote.response.TafseerResponse
import com.giraffe.quranpage.utils.Constants.EndPoints
import com.giraffe.quranpage.utils.Constants.PathSegments.AYAH_INDEX
import com.giraffe.quranpage.utils.Constants.PathSegments.SURAH_INDEX
import com.giraffe.quranpage.utils.Constants.PathSegments.TAFSEER_ID
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface TafseerApiServices {
    @GET("${EndPoints.TAFSIR}/{$TAFSEER_ID}/{$SURAH_INDEX}/{$AYAH_INDEX}")
    suspend fun getTafseer(
        @Path(TAFSEER_ID) tafseerId: Int = 1,
        @Path(SURAH_INDEX) surahIndex: Int,
        @Path(AYAH_INDEX) ayahIndex: Int
    ): Response<TafseerResponse>
}