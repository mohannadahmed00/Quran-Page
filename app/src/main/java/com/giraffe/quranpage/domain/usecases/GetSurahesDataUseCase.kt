package com.giraffe.quranpage.domain.usecases

import com.giraffe.quranpage.domain.repository.Repository
import javax.inject.Inject

class GetSurahesDataUseCase @Inject constructor(private val repository: Repository) {
    private var counterNum = 0
    private var counterCap = 'A'
    private var counterSmall = 'a'
    private var mcsFile = 1

    operator fun invoke() = repository.getSurahesData().map { item ->
        if (counterSmall > 'z') resetCounters()
        item.copy(mcs = getNextMcs(), mcsFile = mcsFile)
    }

    private fun getNextMcs() = when {
        counterNum < 10 -> counterNum.toString().also { counterNum += 2 }
        counterCap <= 'Z' -> counterCap.toString().also { counterCap += 2 }
        else -> counterSmall.toString().also { counterSmall += 2 }
    }

    private fun resetCounters() {
        counterNum = 0
        counterCap = 'A'
        counterSmall = 'a'
        mcsFile++
    }
}