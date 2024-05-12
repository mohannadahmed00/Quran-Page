package com.giraffe.quranpage.repo

import androidx.compose.ui.graphics.Path
import android.util.Log
import androidx.compose.ui.geometry.Offset
import com.giraffe.quranpage.local.LocalDataSource
import com.giraffe.quranpage.local.model.AyahModel
import com.giraffe.quranpage.local.model.PageModel
import com.giraffe.quranpage.remote.RemoteDataSource
import com.giraffe.quranpage.utils.OnResponse
import com.giraffe.quranpage.utils.normalizePoint
import javax.inject.Inject

class Repository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
) {


    fun deleteAll(onDeleted: () -> Unit) {
        localDataSource.deletePages().also {
            onDeleted()
        }
    }

    fun getPages(onResponse: (MutableList<PageModel>) -> Unit) {
        if (localDataSource.getPagesCount() == 0) {
            for (i in 2..49) {
                remoteDataSource.downloadPage(i, object : OnResponse {
                    override fun onSuccess(result: String) {
                        localDataSource.storePageInDatabase(i, result, localDataSource.getAyahs(i))
                        if (i == 49) {
                            onResponse(localDataSource.getPages().toMutableList())
                        }
                    }

                    override fun onFail(errorMsg: String) {
                        Log.e(TAG, "onFail: $errorMsg")
                    }
                })
            }
        } else {
            onResponse(localDataSource.getPages().toMutableList())
        }
    }


    private suspend fun getSurah(surahIndex: Int) {
        val surahResponse = remoteDataSource.getSurah(surahIndex)
        if (surahResponse.isSuccessful) surahResponse.body()
            ?.forEach { localDataSource.storeAyah(it.toAyahModel().copy(surahIndex = surahIndex)) }
    }

    suspend fun getAyahsOfSurah(surahIndex: Int, onResponse: () -> Unit) {
        if (localDataSource.getAyahsCount() == 0) getSurah(surahIndex)
        onResponse()
    }

    fun getAyahs(pageIndex:Int) = localDataSource.getAyahs(pageIndex).toMutableList()


    private fun convertPolygonToPath(page: PageModel, polygon: String): Path {
        val path = Path()
        var flag = true
        Log.e(TAG, "convertPolygonToPath: $polygon")
        if (polygon.isNotBlank()) {
            polygon.split(" ").forEach {
                val x = it.split(",")[0].toFloat()//.dp.toPx()
                val y = it.split(",")[1].toFloat()//.dp.toPx()
                val newPoint = Offset(x, y).normalizePoint(
                    page.image.width,
                    page.image.height,
                    page.pageIndex == 1 || page.pageIndex == 2
                )
                if (flag) {
                    path.moveTo(newPoint.x, newPoint.y)
                    flag = false
                }
                path.lineTo(newPoint.x, newPoint.y)
            }
        }
        return path
    }


    companion object {
        private const val TAG = "Repository"
    }

}