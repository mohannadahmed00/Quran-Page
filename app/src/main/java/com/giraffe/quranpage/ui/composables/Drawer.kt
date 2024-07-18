package com.giraffe.quranpage.ui.composables

import androidx.compose.ui.graphics.vector.ImageVector

data class NavigationItem(
    val title:String,
    val selectedIcon:ImageVector,
    val unselectedIcon:ImageVector,
)