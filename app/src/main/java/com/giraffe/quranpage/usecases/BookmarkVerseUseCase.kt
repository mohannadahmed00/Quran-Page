package com.giraffe.quranpage.usecases

import com.giraffe.quranpage.local.model.VerseModel
import com.giraffe.quranpage.repo.Repository
import javax.inject.Inject

class BookmarkVerseUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(verseModel: VerseModel?) = repository.bookmarkVerse(verseModel)
}