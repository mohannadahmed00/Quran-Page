package com.giraffe.quranpage.data.datasource.remote.api

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url


interface AudioApiService {
    @GET
    @Streaming
    suspend fun downloadAudio(@Url audioUrl: String): ResponseBody
}