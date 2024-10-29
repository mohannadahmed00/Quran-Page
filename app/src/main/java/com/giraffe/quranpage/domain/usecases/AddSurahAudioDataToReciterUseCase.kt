package com.giraffe.quranpage.domain.usecases

import com.giraffe.quranpage.domain.entities.ReciterEntity
import com.giraffe.quranpage.domain.entities.SurahAudioDataEntity
import com.giraffe.quranpage.domain.repository.Repository
import javax.inject.Inject

class AddSurahAudioDataToReciterUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(
        reciterId: Int,
        surahAudioDataEntity: SurahAudioDataEntity
    ): ReciterEntity {
        var reciter = repository.getReciter(reciterId)
        val surahesAudioData = reciter.surahesAudioData.toMutableList()
        surahesAudioData.add(surahAudioDataEntity)
        reciter = reciter.copy(
            surahesAudioData = surahesAudioData
        )
        repository.updateReciterData(reciter)
        return reciter
    }
}