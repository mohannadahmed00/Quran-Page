package com.giraffe.quranpage.remote

import com.giraffe.quranpage.remote.api.ApiServices
import com.giraffe.quranpage.remote.downloader.PageDownloader
import com.giraffe.quranpage.utils.OnResponse
import retrofit2.http.Path

class RemoteDataSource(
    private val apiServices: ApiServices,
    private val pageDownloader: PageDownloader
) {
    suspend fun getSurahesData() = apiServices.getQuranData()
    suspend fun getAyahsOfSurah(surahIndex: Int) = apiServices.getAyahsOfSurah(surahIndex)
    fun downloadPage(pageIndex: Int, onResponse: OnResponse) =
        pageDownloader.download(pageIndex, onResponse)

    suspend fun getTafseer(
        surahIndex: Int,
        ayahNumber: Int
    ) = apiServices.getTafseer(surahIndex = surahIndex, ayahNumber = ayahNumber)
}