package com.giraffe.quranpage.ui.screens.home

import androidx.compose.ui.geometry.Offset
import com.giraffe.quranpage.local.model.PageModel

data class HomeScreenState(
    val pages: MutableList<PageModel> = mutableListOf(),
    val pageIndex: Int = 2,
    val selectedAyahIndex: Int = 0,
    val selectedAyahPolygon: List<Offset> = mutableListOf(),
    val selectedPageIndex: Int = 0,
)
