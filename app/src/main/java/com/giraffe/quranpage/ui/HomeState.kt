package com.giraffe.quranpage.ui

import androidx.compose.ui.geometry.Offset
import androidx.room.Index
import com.giraffe.quranpage.local.model.AyahModel
import com.giraffe.quranpage.local.model.PageModel

data class HomeState(
    val pages: MutableList<PageModel> = mutableListOf(),
    val pageIndex: Int = 2,
    val selectedAyahIndex: Int = 0,
    val selectedAyahPolygon: List<Offset> = mutableListOf(),
    val selectedPageIndex: Int = 0,
)
