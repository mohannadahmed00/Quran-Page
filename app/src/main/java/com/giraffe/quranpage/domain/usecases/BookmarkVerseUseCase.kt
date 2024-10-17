package com.giraffe.quranpage.domain.usecases

import com.giraffe.quranpage.domain.entities.VerseEntity
import com.giraffe.quranpage.domain.repository.Repository
import javax.inject.Inject

class BookmarkVerseUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(verse: VerseEntity) = repository.bookmarkVerse(verse)
}