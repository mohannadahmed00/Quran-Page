package com.giraffe.quranpage.repo

import android.util.Log
import com.giraffe.quranpage.local.LocalDataSource
import com.giraffe.quranpage.local.model.AyahModel
import com.giraffe.quranpage.local.model.PageModel
import com.giraffe.quranpage.local.model.SurahDataModel
import com.giraffe.quranpage.remote.RemoteDataSource
import com.giraffe.quranpage.utils.OnResponse
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
        surahData: SurahDataModel,
        ayahs: List<AyahModel>?,
        onResponse: (MutableList<PageModel>) -> Unit
    ) {
        for (pageIndex in surahData.startPage..surahData.endPage) {
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

    private suspend fun getSurahData(surahIndex: Int): SurahDataModel {
        if (localDataSource.getCountOfSurahesData() == 0) {
            val surahesDataResponse = remoteDataSource.getSurahesData()
            if (surahesDataResponse.isSuccessful) surahesDataResponse.body()?.surahesData
                ?.forEach {
                    localDataSource.storeSurahData(
                        it.toSurahDataModel()
                    )
                }
        }
        return localDataSource.getSurahData(surahIndex)
    }

    private suspend fun getAyahsOfSurah(surahIndex: Int) =
        remoteDataSource.getAyahsOfSurah(surahIndex).body()
            ?.map {
                it.toAyahModel().copy(surahIndex = surahIndex)
            }

    fun getContentOfPage(pageIndex:Int) = localDataSource.getContentOfPage(pageIndex)

    companion object {
        private const val TAG = "Repository"
    }
}