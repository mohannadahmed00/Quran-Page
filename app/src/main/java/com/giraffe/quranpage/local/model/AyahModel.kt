package com.giraffe.quranpage.local.model

import android.graphics.Bitmap
import androidx.compose.ui.geometry.Offset
import com.giraffe.quranpage.utils.isSmallPage
import com.giraffe.quranpage.utils.normalizePoint

data class AyahModel(
    val surahIndex: Int = 0,
    val ayahIndex: Int,
    val endTime: Int,
    val pageIndex: Int,
    val polygon: List<Offset>,
    val startTime: Int,
    val ayahIndexPosition: Offset,
) {
    fun normalizeAyahIndexPosition(bitmap: Bitmap) = this.copy(
        ayahIndexPosition = this.ayahIndexPosition.normalizePoint(
            bitmap.width,
            bitmap.height,
            isSmallPage(pageIndex)
        ),
    )
}
