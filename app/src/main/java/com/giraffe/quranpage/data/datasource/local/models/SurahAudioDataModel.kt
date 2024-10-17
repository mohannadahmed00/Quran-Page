package com.giraffe.quranpage.data.datasource.local.models

data class SurahAudioDataModel(
    val surahIndex:Int,
    val audioPath:String,
    val verseTiming: List<VerseTimingModel>
)
