package com.giraffe.quranpage.data.repository

import androidx.compose.runtime.toMutableStateList
import com.giraffe.quranpage.common.service.DownloadService
import com.giraffe.quranpage.common.utils.addOrUpdate
import com.giraffe.quranpage.common.utils.isNetworkAvailable
import com.giraffe.quranpage.data.datasource.local.LocalDataSource
import com.giraffe.quranpage.data.datasource.local.models.SurahAudioDataModel
import com.giraffe.quranpage.data.datasource.remote.RemoteDataSource
import com.giraffe.quranpage.data.toEntity
import com.giraffe.quranpage.data.toModel
import com.giraffe.quranpage.domain.entities.ReciterEntity
import com.giraffe.quranpage.domain.entities.VerseEntity
import com.giraffe.quranpage.domain.repository.Repository
import javax.inject.Inject

class RepositoryImp @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
) : Repository {
    override suspend fun bookmarkVerse(verse: VerseEntity) =
        localDataSource.bookmarkVerse(verse.toModel())

    override suspend fun getBookmarkedVerse() = localDataSource.getBookmarkedVerse()?.toEntity()
    override suspend fun removeBookmarkedVerse() = localDataSource.removeBookmarkedVerse()
    override suspend fun saveLastPageIndex(pageIndex: Int) =
        localDataSource.saveLastPageIndex(pageIndex)

    override suspend fun getLastPageIndex() = localDataSource.getLastPageIndex()
    override fun getAllVerses() = localDataSource.getAllVerses().map { it.toEntity() }
    override fun getSurahesData() = localDataSource.getSurahesData().map { it.toEntity() }
    override suspend fun getTafseer(
        surahIndex: Int,
        verseIndex: Int,
    ) = remoteDataSource.getTafseer(surahIndex = surahIndex, ayahIndex = verseIndex).let {
        if (it.isSuccessful) it.body()?.toEntity() else null
    }

    override suspend fun getReciters(): List<ReciterEntity> {
        if (isNetworkAvailable()) {
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
        return localDataSource.getAllReciters().map { it.toEntity() }
    }

    override suspend fun saveAudioData(
        downloadedAudio: DownloadService.DownloadedAudio,
    ): ReciterEntity {
        remoteDataSource.getVersesTimingOfSurah(
            downloadedAudio.surahId,
            downloadedAudio.reciterId
        ).let { surahAudioDataResponse ->
            val responseBody = surahAudioDataResponse.body()
            if (surahAudioDataResponse.isSuccessful && responseBody != null) {
                val surahAudioDataModel = SurahAudioDataModel(
                    surahIndex = downloadedAudio.surahId,
                    audioPath = downloadedAudio.filePath,
                    verseTiming = responseBody.map { it.toModel() }
                )
                val reciter = localDataSource.getReciter(downloadedAudio.reciterId)
                val newReciter = reciter.copy(
                    surahesAudioData = reciter.surahesAudioData.toMutableStateList()
                        .addOrUpdate(surahAudioDataModel)
                )
                localDataSource.storeReciter(newReciter)
            }
        }
        return localDataSource.getReciter(downloadedAudio.reciterId).toEntity()
    }
}