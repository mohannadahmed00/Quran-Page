package com.giraffe.quranpage.domain.usecases

import com.giraffe.quranpage.domain.repository.Repository
import javax.inject.Inject

class GetVersesTimingUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(surahIndex: Int, reciterId: Int) = repository.getVersesTiming(
        surahIndex = surahIndex,
        reciterId = reciterId
    )
}