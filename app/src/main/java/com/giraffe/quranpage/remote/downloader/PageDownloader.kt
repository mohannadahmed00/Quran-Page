package com.giraffe.quranpage.remote.downloader

import com.giraffe.quranpage.utils.Constants.PAGES_URL
import com.giraffe.quranpage.utils.OnResponse
import com.giraffe.quranpage.utils.toThreeDigits
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class PageDownloader(private val httpClient: OkHttpClient.Builder) {
    fun download(pageIndex: Int, onResponse: OnResponse) {
        val request = Request.Builder()
            .url(getPageUrl(pageIndex))
            .build()
        httpClient.build().newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val content = response.body?.string()
                onResponse.onSuccess(content ?: "")
            }

            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                onResponse.onFail(e.message ?: "IOException: download file error !!")
            }
        })
    }

    private fun getPageUrl(pageIndex: Int) = PAGES_URL.replace(
        "000",
        if (PAGES_URL.contains("mp3quran")) pageIndex.toThreeDigits() else pageIndex.toString()
    )
}