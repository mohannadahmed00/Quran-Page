package com.giraffe.quranpage.local.model

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.giraffe.quranpage.utils.Constants

@Entity(tableName = Constants.DatabaseTables.PAGES)
data class PageModel(
    @PrimaryKey
    val pageIndex: Int,
    val image: Bitmap,
    //val ayahs:List<AyahModel>
)
