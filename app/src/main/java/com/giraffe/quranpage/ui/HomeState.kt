package com.giraffe.quranpage.ui

import androidx.room.Index
import com.giraffe.quranpage.local.model.AyahModel
import com.giraffe.quranpage.local.model.PageModel

data class HomeState(
    val pages: MutableList<PageModel> = mutableListOf(),
    val ayahs: MutableList<AyahModel> = mutableListOf(),
    val pageIndex: Int = 2
)
