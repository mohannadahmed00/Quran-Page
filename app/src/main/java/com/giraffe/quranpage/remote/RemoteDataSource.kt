package com.giraffe.quranpage.remote

import android.util.Log
import com.giraffe.quranpage.local.model.SurahAudioModel
import com.giraffe.quranpage.remote.api.RecitersApiServices
import com.giraffe.quranpage.remote.api.TafseerApiServices
import com.giraffe.quranpage.remote.downloader.FileDownloader
import com.giraffe.quranpage.remote.downloader.PageDownloader
import com.giraffe.quranpage.service.DownloadService
import com.giraffe.quranpage.utils.OnResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RemoteDataSource(
    private val tafseerApiServices: TafseerApiServices,
    private val recitersApiServices: RecitersApiServices,
    private val pageDownloader: PageDownloader,
    private val fileDownloader: FileDownloader,
) {
    suspend fun getAyahsTimingOfSurah(surahIndex: Int) =
        recitersApiServices.getAyahsOfSurah(surahIndex)

    fun downloadPage(pageIndex: Int, onResponse: OnResponse) =
        pageDownloader.download(pageIndex, onResponse)

    suspend fun getTafseer(
        surahIndex: Int,
        ayahIndex: Int
    ) = tafseerApiServices.getTafseer(surahIndex = surahIndex, ayahIndex = ayahIndex)

    suspend fun getReciters() = recitersApiServices.getReciters()
    fun downloadSurahAudio(
        reciterId: Int,
        folderUrl: String,
        surahIndex: Int,
        onComplete: (SurahAudioModel?) -> Unit
    ) =
        fileDownloader.download(folderUrl, surahIndex) { audioPath ->
            if (!audioPath.isNullOrBlank()) {
                Log.d("TAG", "QuranContent -1: $audioPath")

                CoroutineScope(Dispatchers.IO).launch {
                    recitersApiServices.getAyahsOfSurah(surahIndex, reciterId).let { response ->
                        if (!response.isSuccessful || response.body() == null) {
                            //error with ayahsTiming response
                            onComplete(null)
                        } else {
                            val surahAudioModel = SurahAudioModel(
                                surahIndex, audioPath, response.body() ?: emptyList()
                            )
                            onComplete(surahAudioModel)
                        }
                    }
                }

            } else {
                //error with audio file path
                onComplete(null)
            }
        }

    fun getSurahAudioData(
        reciterId: Int,
        audioPath: String,
        surahIndex: Int, onComplete: (SurahAudioModel?) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            recitersApiServices.getAyahsOfSurah(surahIndex, reciterId).let { response ->
                if (!response.isSuccessful || response.body() == null) {
                    onComplete(null)
                } else {
                    val surahAudioModel = SurahAudioModel(
                        surahIndex, audioPath, response.body() ?: emptyList()
                    )
                    onComplete(surahAudioModel)
                }
            }
        }
    }
}