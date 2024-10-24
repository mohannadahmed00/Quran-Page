package com.giraffe.quranpage.data.repository

import androidx.compose.runtime.toMutableStateList
import com.giraffe.quranpage.common.service.DownloadService
import com.giraffe.quranpage.common.utils.addOrUpdate
import com.giraffe.quranpage.common.utils.domain.Resource
import com.giraffe.quranpage.common.utils.domain.map
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
    ) = remoteDataSource.getTafseer(surahIndex = surahIndex, ayahIndex = verseIndex).map {
        it.toEntity()
    }

    override suspend fun getReciters(): List<ReciterEntity> {
        return when (val result = remoteDataSource.getReciters()) {
            is Resource.Error -> {

                val reciters = localDataSource.getAllReciters()
                    .map { reciterModel -> reciterModel.toEntity() }
                reciters
            }

            is Resource.Success -> {
                val localRecitersCount = localDataSource.getRecitersCount()
                if (result.data.size > localRecitersCount) {
                    val oldReciters = localDataSource.getAllReciters()
                    val newReciters =
                        result.data.filter { newReciter -> oldReciters.firstOrNull { it.id == newReciter.id } == null }
                    newReciters.forEach { reciterResponse ->
                        localDataSource.storeReciter(reciterResponse.toModel())
                    }
                }
                val reciters = localDataSource.getAllReciters()
                    .map { reciterModel -> reciterModel.toEntity() }
                reciters
            }
        }
    }

    override suspend fun saveAudioData(
        downloadedAudio: DownloadService.DownloadedAudio,
    ): ReciterEntity {
        return when (val result = remoteDataSource.getVersesTimingOfSurah(
            downloadedAudio.surahId,
            downloadedAudio.reciterId
        )) {
            is Resource.Success -> {
                val surahAudioDataModel = SurahAudioDataModel(
                    surahIndex = downloadedAudio.surahId,
                    audioPath = downloadedAudio.filePath,
                    verseTiming = result.data.map { it.toModel() }
                )
                var reciter = localDataSource.getReciter(downloadedAudio.reciterId)
                reciter = reciter.copy(
                    surahesAudioData = reciter.surahesAudioData.toMutableStateList()
                        .addOrUpdate(surahAudioDataModel)
                )
                localDataSource.storeReciter(reciter)
                reciter.toEntity()
            }

            is Resource.Error -> localDataSource.getReciter(downloadedAudio.reciterId).toEntity()
        }
    }
}