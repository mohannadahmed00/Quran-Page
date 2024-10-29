package com.giraffe.quranpage.data.repository

import com.giraffe.quranpage.common.utils.domain.NetworkError
import com.giraffe.quranpage.common.utils.domain.Resource
import com.giraffe.quranpage.common.utils.domain.map
import com.giraffe.quranpage.data.datasource.local.LocalDataSource
import com.giraffe.quranpage.data.datasource.remote.RemoteDataSource
import com.giraffe.quranpage.data.toEntity
import com.giraffe.quranpage.data.toModel
import com.giraffe.quranpage.domain.entities.ReciterEntity
import com.giraffe.quranpage.domain.entities.VerseEntity
import com.giraffe.quranpage.domain.entities.VerseTimingEntity
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
    override suspend fun getTafseer(verse: VerseEntity) =
        remoteDataSource.getTafseer(surahIndex = verse.surahIndex, ayahIndex = verse.verseIndex)
            .map {
                it.toEntity()
            }

    override suspend fun getVersesTiming(
        surahIndex: Int,
        reciterId: Int
    ): Resource<List<VerseTimingEntity>, NetworkError> {
        return remoteDataSource.getVersesTimingOfSurah(
            surahIndex,
            reciterId
        ).map { list -> list.map { it.toEntity() } }
    }

    override suspend fun getReciters(): List<ReciterEntity> {
        return when (val result = remoteDataSource.getReciters()) {
            is Resource.Error -> localDataSource.getAllReciters()
                .map { reciterModel -> reciterModel.toEntity() }

            is Resource.Success -> {
                val oldReciterIds = localDataSource.getAllReciters().map { it.id }
                result.data.filter { it.id !in oldReciterIds }.forEach { newReciter ->
                    localDataSource.storeReciter(newReciter.toModel())
                }
                localDataSource.getAllReciters()
                    .map { reciterModel -> reciterModel.toEntity() }
            }
        }
    }

    override suspend fun getReciter(reciterId: Int) =
        localDataSource.getReciter(reciterId).toEntity()

    override suspend fun updateReciterData(reciter: ReciterEntity) =
        localDataSource.storeReciter(reciter.toModel())
}