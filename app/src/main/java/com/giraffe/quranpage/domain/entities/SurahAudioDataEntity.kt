package com.giraffe.quranpage.domain.entities

data class SurahAudioDataEntity(
    val surahIndex: Int,
    val audioPath: String,
    val verseTiming: List<VerseTimingEntity>
)
