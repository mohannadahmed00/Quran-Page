package com.giraffe.quranpage.usecases

import com.giraffe.quranpage.repo.Repository
import javax.inject.Inject

class GetSurahesDataUseCase @Inject constructor(private val repository: Repository) {
    operator fun invoke() = repository.getSurahesData()
}