package com.giraffe.quranpage.domain.entities

data class ReciterEntity(
    val id: Int,
    val name: String,
    val folderUrl: String,
    val rewaya: String,
    val surahesAudioData: List<SurahAudioDataEntity> = emptyList()
)
