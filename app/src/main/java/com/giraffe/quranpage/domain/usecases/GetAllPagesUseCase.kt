package com.giraffe.quranpage.domain.usecases

import com.giraffe.quranpage.common.utils.presentation.getHezb
import com.giraffe.quranpage.common.utils.presentation.getNamesOfSurahes
import com.giraffe.quranpage.common.utils.presentation.hasSajdah
import com.giraffe.quranpage.domain.entities.ContentEntity
import com.giraffe.quranpage.domain.entities.PageEntity
import com.giraffe.quranpage.domain.repository.Repository
import javax.inject.Inject


class GetAllPagesUseCase @Inject constructor(private val repository: Repository) {
    operator fun invoke(): List<PageEntity> {
        val verses = repository.getAllVerses()
        var currentHezbNumber = 1
        return verses.groupBy { it.pageIndex }.toList().map { versesByPage ->
            val pageHezbNumber = versesByPage.second.last().quarterHezbIndex
            val pageIndex = versesByPage.first
            val contents =
                versesByPage.second.groupBy { verses -> verses.surahIndex }.map { content ->
                    val versesOfContent = content.value
                    ContentEntity(
                        surahNameAr = content.value.first().surahNameAr,
                        verses = versesOfContent,
                        pageIndex = pageIndex
                    )
                }
            PageEntity(
                contents = contents,
                orgContents = contents,
                pageIndex = pageIndex,
                namesOfSurahes = getNamesOfSurahes(contents),
                juz = versesByPage.second.first().juz,
                hezbStr = getHezb(
                    currentHezbNumber,
                    pageHezbNumber
                ) { hezb -> currentHezbNumber = hezb },
                hasSajdah = hasSajdah(contents)
            )
        }
    }
}