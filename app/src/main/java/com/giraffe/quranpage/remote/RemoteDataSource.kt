package com.giraffe.quranpage.remote

import com.giraffe.quranpage.remote.api.RecitersApiServices
import com.giraffe.quranpage.remote.api.TafseerApiServices
import com.giraffe.quranpage.remote.downloader.PageDownloader
import com.giraffe.quranpage.utils.OnResponse

class RemoteDataSource(
    private val tafseerApiServices: TafseerApiServices,
    private val recitersApiServices: RecitersApiServices,
    private val pageDownloader: PageDownloader
) {
    suspend fun getAyahsTimingOfSurah(surahIndex: Int) = recitersApiServices.getAyahsOfSurah(surahIndex)
    fun downloadPage(pageIndex: Int, onResponse: OnResponse) =
        pageDownloader.download(pageIndex, onResponse)

    suspend fun getTafseer(
        surahIndex: Int,
        ayahIndex: Int
    ) = tafseerApiServices.getTafseer(surahIndex = surahIndex, ayahIndex = ayahIndex)

    suspend fun getReciters() = recitersApiServices.getReciters()
}