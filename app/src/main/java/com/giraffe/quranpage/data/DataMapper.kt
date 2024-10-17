package com.giraffe.quranpage.data

import com.giraffe.quranpage.data.datasource.local.models.ReciterModel
import com.giraffe.quranpage.data.datasource.local.models.SurahAudioDataModel
import com.giraffe.quranpage.data.datasource.local.models.SurahDataModel
import com.giraffe.quranpage.data.datasource.local.models.VerseModel
import com.giraffe.quranpage.data.datasource.local.models.VerseTimingModel
import com.giraffe.quranpage.data.datasource.remote.responses.ReciterResponse
import com.giraffe.quranpage.data.datasource.remote.responses.TafseerResponse
import com.giraffe.quranpage.data.datasource.remote.responses.VerseTimingResponse
import com.giraffe.quranpage.domain.entities.ReciterEntity
import com.giraffe.quranpage.domain.entities.SurahAudioDataEntity
import com.giraffe.quranpage.domain.entities.SurahDataEntity
import com.giraffe.quranpage.domain.entities.TafseerEntity
import com.giraffe.quranpage.domain.entities.VerseEntity
import com.giraffe.quranpage.domain.entities.VerseTimingEntity

fun VerseTimingResponse.toModel() =
    VerseTimingModel(
        verseIndex = verseIndex,
        startTime = startTime,
        endTime = endTime
    )

fun ReciterResponse.toModel() = ReciterModel(
    id,
    name,
    folderUrl,
    rewaya
)


fun VerseModel.toEntity() = VerseEntity(
    content = content,
    normalContent = normalContent,
    qcfContent = qcfData,
    surahIndex = surahNumber,
    verseIndex = verseNumber,
    pageIndex = pageIndex,
    quarterHezbIndex = quarterHezbIndex,
    hasSajda = sajda
)

fun VerseEntity.toModel() = VerseModel(
    content = content,
    normalContent = normalContent,
    qcfData = qcfContent,
    surahNumber = surahIndex,
    verseNumber = verseIndex,
    pageIndex = pageIndex,
    quarterHezbIndex = quarterHezbIndex,
    sajda = hasSajda
)

fun ReciterModel.toEntity() = ReciterEntity(
    id = id,
    name = name,
    folderUrl = folderUrl,
    rewaya = rewaya,
    surahesAudioData = surahesAudioData.map { it.toEntity() }
)

fun SurahAudioDataModel.toEntity() = SurahAudioDataEntity(
    surahIndex = surahIndex,
    audioPath = audioPath,
    verseTiming = verseTiming.map { it.toEntity() }
)

fun VerseTimingModel.toEntity() = VerseTimingEntity(
    verseIndex = verseIndex,
    startTime = startTime,
    endTime = endTime
)

fun TafseerResponse.toEntity() = TafseerEntity(
    id = id,
    verseIndex = verseIndex,
    name = name,
    text = text
)

fun SurahDataModel.toEntity() = SurahDataEntity(
    id = id,
    arabicName = arabic,
    englishName = name,
    turkishName = turkish,
    countOfVerses = aya,
    startPageIndex = startPage,
    endPageIndex = endPage,
    place = place
)
