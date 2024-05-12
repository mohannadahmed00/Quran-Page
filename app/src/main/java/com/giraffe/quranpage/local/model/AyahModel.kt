package com.giraffe.quranpage.local.model

import androidx.compose.ui.graphics.Path
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.giraffe.quranpage.utils.Constants

@Entity(tableName = Constants.DatabaseTables.AYAHS)
data class AyahModel(
    @PrimaryKey(true)
    val id: Int = 0,
    val surahIndex:Int = 0,
    val ayah: Int,
    val endTime: Int,
    val pageIndex: Int,
    val polygon: String,
    val startTime: Int,
    val x: Float,
    val y: Float,
)
