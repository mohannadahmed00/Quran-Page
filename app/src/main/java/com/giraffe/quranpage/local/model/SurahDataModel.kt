package com.giraffe.quranpage.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.giraffe.quranpage.utils.Constants.DatabaseTables

@Entity(tableName = DatabaseTables.SURAHES_DATA)
data class SurahDataModel(
    @PrimaryKey
    val id: Int,
    val endPage: Int,
    val makkia: Int,
    val name: String,
    val startPage: Int,
    val type: Int
)