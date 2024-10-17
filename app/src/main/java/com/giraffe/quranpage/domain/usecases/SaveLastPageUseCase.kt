package com.giraffe.quranpage.domain.usecases

import com.giraffe.quranpage.domain.repository.Repository
import javax.inject.Inject

class SaveLastPageUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(pageIndex: Int) = repository.saveLastPageIndex(pageIndex)
}