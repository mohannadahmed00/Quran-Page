package com.giraffe.quranpage.data.datasource.local.models

import androidx.compose.runtime.Immutable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.giraffe.quranpage.common.utils.Constants

@Entity(tableName = Constants.DatabaseTables.RECITERS)
@Immutable
data class ReciterModel(
    @PrimaryKey
    val id: Int,
    val name: String,
    val folderUrl: String,
    val rewaya: String,
    val surahesAudioData:List<SurahAudioDataModel> = emptyList()
)


