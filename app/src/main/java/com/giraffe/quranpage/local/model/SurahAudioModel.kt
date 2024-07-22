package com.giraffe.quranpage.local.model

import com.giraffe.quranpage.remote.response.AyahResponse

data class SurahAudioModel(
    val surahId:Int,
    val audioPath:String,
    val ayahsTiming: List<AyahResponse>
)
