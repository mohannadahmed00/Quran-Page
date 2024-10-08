package com.giraffe.quranpage.remote.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url


interface AudioService {
    @GET
    @Streaming
   suspend fun downloadAudio(@Url audioUrl: String):ResponseBody
}