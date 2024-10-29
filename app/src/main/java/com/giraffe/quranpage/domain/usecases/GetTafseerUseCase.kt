package com.giraffe.quranpage.domain.usecases

import com.giraffe.quranpage.domain.entities.VerseEntity
import com.giraffe.quranpage.domain.repository.Repository
import javax.inject.Inject

class GetTafseerUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(verse: VerseEntity) = repository.getTafseer(verse = verse)
}