package com.giraffe.quranpage.domain.usecases

import com.giraffe.quranpage.domain.repository.Repository
import javax.inject.Inject

class RemoveBookmarkedVerseUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke() = repository.removeBookmarkedVerse()
}