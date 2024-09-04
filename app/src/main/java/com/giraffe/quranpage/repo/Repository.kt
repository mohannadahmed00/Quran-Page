package com.giraffe.quranpage.repo

import android.util.Log
import androidx.compose.runtime.toMutableStateList
import com.giraffe.quranpage.local.LocalDataSource
import com.giraffe.quranpage.local.model.AyahModel
import com.giraffe.quranpage.local.model.PageModel
import com.giraffe.quranpage.local.model.ReciterModel
import com.giraffe.quranpage.local.model.SurahAudioModel
import com.giraffe.quranpage.local.model.SurahModel
import com.giraffe.quranpage.remote.RemoteDataSource
import com.giraffe.quranpage.service.DownloadService
import com.giraffe.quranpage.utils.OnResponse
import com.giraffe.quranpage.utils.addOrUpdate
import javax.inject.Inject

class Repository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
) {
    suspend fun getPagesOfSurah(surahIndex: Int, onResponse: (MutableList<PageModel>) -> Unit) {
        val surahData = getSurahData(surahIndex)
        if (localDataSource.getPagesCount() == 0) {
            val ayahs = getAyahsOfSurah(surahIndex)
            downloadPages(surahData, ayahs, onResponse)
        } else {
            onResponse(localDataSource.getPages().toMutableList())
        }
    }

    private fun downloadPages(
        surahData: SurahModel?,
        ayahs: List<AyahModel>?,
        onResponse: (MutableList<PageModel>) -> Unit
    ) {
        surahData?.let {
            for (pageIndex in it.startPage..it.endPage) {
                remoteDataSource.downloadPage(pageIndex, object : OnResponse {
                    override fun onSuccess(result: String) {
                        storePage(
                            svgData = result,
                            ayahs = ayahs,
                            pageIndex = pageIndex,
                            endPage = surahData.endPage,
                            onResponse = onResponse
                        )
                    }

                    override fun onFail(errorMsg: String) {
                        Log.e(TAG, "onFail: $errorMsg")
                    }
                })
            }
        }

    }

    private fun storePage(
        svgData: String,
        ayahs: List<AyahModel>?,
        pageIndex: Int,
        endPage: Int,
        onResponse: (MutableList<PageModel>) -> Unit
    ) {
        localDataSource.storePageInDatabase(
            svgData = svgData,
            ayahs = ayahs?.filter { it.pageIndex == pageIndex } ?: emptyList(),
            pageIndex = pageIndex,
        )
        if (pageIndex == endPage) {
            onResponse(localDataSource.getPages().toMutableList())
        }
    }

    private fun getSurahData(surahIndex: Int): SurahModel? {
        return localDataSource.getSurahData(surahIndex)
    }

    private suspend fun getAyahsOfSurah(surahIndex: Int) =
        remoteDataSource.getAyahsTimingOfSurah(surahIndex).body()
            ?.map {
                it.toAyahModel().copy(surahIndex = surahIndex)
            }

    fun getAllVerses() = localDataSource.getAllVerses()
    fun getSurahesData() = localDataSource.getSurahesData()


    suspend fun getTafseer(
        surahIndex: Int,
        ayahIndex: Int
    ) = remoteDataSource.getTafseer(surahIndex = surahIndex, ayahIndex = ayahIndex).let {
        if (it.isSuccessful) it.body() else null
    }

    suspend fun getReciters(isConnected: Boolean): List<ReciterModel> {
        // && localDataSource.getRecitersCount()==0
        if (isConnected) {
            remoteDataSource.getReciters().let {
                if (it.isSuccessful) {
                    val remoteRecitersCount = it.body()?.count() ?: 0
                    val localRecitersCount = localDataSource.getRecitersCount()
                    if (remoteRecitersCount > localRecitersCount) {
                        it.body()?.forEach { reciterResponse ->
                            localDataSource.storeReciter(reciterResponse.toModel())
                        }
                    }
                }
            }
        }
        return localDataSource.getAllReciters()
    }

    fun saveAudioFile(
        downloadedAudio: DownloadService.DownloadedAudio,
        onComplete: (ReciterModel, SurahAudioModel?) -> Unit
    ) {
        remoteDataSource.getSurahAudioData(
            downloadedAudio.reciterId,
            downloadedAudio.filePath,
            downloadedAudio.surahIndex
        ) { surahAudioModel ->
            if (surahAudioModel != null) {
                val reciter = localDataSource.getReciter(downloadedAudio.reciterId)
                val newReciter = reciter.copy(
                    surahesAudioData = reciter.surahesAudioData.toMutableStateList()
                        .addOrUpdate(surahAudioModel)
                )
                localDataSource.storeReciter(newReciter)
            }

            onComplete(localDataSource.getReciter(downloadedAudio.reciterId), surahAudioModel)
        }
    }

    fun downloadSurahAudio(
        reciterId: Int,
        folderUrl: String,
        surahIndex: Int,
        onComplete: (List<ReciterModel>) -> Unit
    ) {
        remoteDataSource.downloadSurahAudio(
            reciterId,
            folderUrl,
            surahIndex
        ) { surahAudioModel ->
            if (surahAudioModel != null) {
                val reciter = localDataSource.getReciter(reciterId)
                reciter.surahesAudioData.toMutableStateList().addOrUpdate(surahAudioModel)
                localDataSource.storeReciter(
                    reciter.copy(
                        surahesAudioData = reciter.surahesAudioData.toMutableStateList()
                            .addOrUpdate(surahAudioModel)
                    )
                )

            }
            onComplete(localDataSource.getAllReciters())
        }
    }


    private fun updateOrAddSurahAudio(
        surahList: MutableList<SurahAudioModel>,
        newSurahAudio: SurahAudioModel
    ): List<SurahAudioModel> {
        val index = surahList.indexOfFirst { it.surahId == newSurahAudio.surahId }
        if (index != -1) {
            surahList[index] = newSurahAudio
        } else {
            surahList.add(newSurahAudio)
        }
        return surahList
    }

    companion object {
        private const val TAG = "Repository"
    }
}