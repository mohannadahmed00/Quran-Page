package com.giraffe.quranpage.domain.usecases

import com.giraffe.quranpage.domain.repository.Repository
import javax.inject.Inject

class GetLastPageUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke() = repository.getLastPageIndex().let {
        if (it == 0) 1 else it
    }
}