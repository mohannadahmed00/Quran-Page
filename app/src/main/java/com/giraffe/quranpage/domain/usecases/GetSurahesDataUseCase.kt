package com.giraffe.quranpage.domain.usecases

import com.giraffe.quranpage.domain.repository.Repository
import javax.inject.Inject

class GetSurahesDataUseCase @Inject constructor(private val repository: Repository) {
    operator fun invoke() = repository.getSurahesData()
}