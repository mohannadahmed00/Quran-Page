package com.giraffe.quranpage.usecases

import com.giraffe.quranpage.repo.Repository
import javax.inject.Inject

class GetBookmarkedVerseUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke() = repository.getBookmarkedVerse()
}