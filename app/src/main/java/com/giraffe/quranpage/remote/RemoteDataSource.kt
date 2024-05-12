package com.giraffe.quranpage.remote

import com.giraffe.quranpage.remote.api.ApiServices
import com.giraffe.quranpage.utils.PageDownloader
import com.giraffe.quranpage.utils.OnResponse

class RemoteDataSource(
    private val apiServices: ApiServices,
    private val pageDownloader: PageDownloader
) {
    suspend fun getSurah(surahIndex: Int) = apiServices.getSurah(surahIndex)
    fun downloadPage(pageIndex: Int, onResponse: OnResponse) =
        pageDownloader.download(pageIndex, onResponse)
}