package com.giraffe.quranpage.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.core.content.ContextCompat
import com.caverock.androidsvg.SVG
import com.giraffe.quranpage.R
import com.giraffe.quranpage.local.model.AyahModel
import com.giraffe.quranpage.local.model.ReciterModel
import com.giraffe.quranpage.local.model.SurahAudioModel
import com.giraffe.quranpage.utils.Constants.PageDimensions
import java.io.IOException

fun MutableList<SurahAudioModel>.addOrUpdate(item: SurahAudioModel): MutableList<SurahAudioModel> {
    val index = indexOfFirst { it.surahId == item.surahId }
    if (index >= 0) set(index, item) else add(item)
    return this
}

fun MutableList<ReciterModel>.addOrUpdate(item: ReciterModel?): List<ReciterModel> {
    item?.let {
        val index = indexOfFirst { it.id == item.id }
        if (index >= 0) set(index, item) else add(item)
    }
    return this
}

fun SnapshotStateList<ReciterModel>.addOrUpdate(list: SnapshotStateList<ReciterModel>): SnapshotStateList<ReciterModel> {
    list.forEach { item ->
        val index = indexOfFirst { it.id == item.id }
        if (index >= 0) set(index, item) else add(item)
    }
    return this
}

fun isNetworkAvailable(): Boolean {
    val runtime = Runtime.getRuntime()
    try {
        val ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8")
        val exitValue = ipProcess.waitFor()
        return exitValue == 0
    } catch (e: IOException) {
        e.printStackTrace()
    } catch (e: InterruptedException) {
        e.printStackTrace()
    }
    return false
}

fun Int.toThreeDigits(): String {
    var intStr = this.toString()
    while (intStr.length < 3) {
        intStr = "0".plus(intStr)
    }
    return intStr
}

fun Offset.normalizePoint(newWidth: Int, newHeight: Int, isSmall: Boolean) =
    Offset(
        x * (newWidth / if (isSmall) PageDimensions.SMALL_WIDTH else PageDimensions.NORMAL_WIDTH),
        y * (newHeight / if (isSmall) PageDimensions.SMALL_HEIGHT else PageDimensions.NORMAL_HEIGHT)
    )

fun Offset.normalizePoint(newWidth: Float, newHeight: Float, isSmall: Boolean) =
    Offset(
        x * (newWidth / if (isSmall) PageDimensions.SMALL_WIDTH else PageDimensions.NORMAL_WIDTH),
        y * (newHeight / if (isSmall) PageDimensions.SMALL_HEIGHT else PageDimensions.NORMAL_HEIGHT)
    )

fun Bitmap.drawCircles(context: Context, ayahs: List<AyahModel>, pageIndex: Int) {
    ayahs.forEach {
        if (it.ayahIndexPosition.x != 0f || it.ayahIndexPosition.y != 0f) {
            val canvas = Canvas(this)
            val drawableId = R.drawable.ayah_end
            val drawable = ContextCompat.getDrawable(context, drawableId)
            drawable?.setTint(ContextCompat.getColor(context, R.color.teal_700))
            val width = if (isSmallPage(pageIndex)) 35 else 55
            val height = if (isSmallPage(pageIndex)) 40 else 63
            val left = it.ayahIndexPosition.x - (width / 2)
            val top = it.ayahIndexPosition.y - (height / 2)
            val right = left + width
            val bottom = top + height
            val destinationRect = Rect(left.toInt(), top.toInt(), right.toInt(), bottom.toInt())
            drawable?.bounds = destinationRect
            drawable?.draw(canvas)
            canvas.save()
        }
    }
}

fun SVG.renderSvgToBitmap(isSmall: Boolean = false): Bitmap {
    val bitmap = Bitmap.createBitmap(
        if (isSmall) PageDimensions.SMALL_WIDTH * 3 else PageDimensions.NORMAL_WIDTH * 3,
        if (isSmall) PageDimensions.SMALL_HEIGHT * 3 else PageDimensions.NORMAL_HEIGHT * 3,
        Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    this.renderToCanvas(canvas)
    return bitmap
}


//==============================================utils===============================================
fun isPointInsidePolygon(point: Offset, polygon: List<Offset>): Boolean {
    val (x, y) = point
    val n = polygon.size
    var inside = false
    for (i in 0 until n) {
        val (x1, y1) = polygon[i]
        val (x2, y2) = polygon[(i + 1) % n]
        if ((y1 < y && y <= y2 || y2 < y && y <= y1) && x < (x2 - x1) * (y - y1) / (y2 - y1) + x1) {
            inside = !inside
        }
    }
    return inside
}

fun normalizeAyahPolygon(ayah: AyahModel, imageSize: Size) = ayah.polygon.map {
    it.normalizePoint(
        imageSize.width,
        imageSize.height,
        ayah.pageIndex == 1 || ayah.pageIndex == 2
    )
}

fun getSelectedPath(
    offset: Offset,
    ayahs: List<AyahModel>,
    imageSize: Size
): Pair<Int, List<Offset>>? {
    for ((index, ayah) in ayahs.withIndex()) {
        val polygon = normalizeAyahPolygon(ayah, imageSize)
        val isBelong = isPointInsidePolygon(offset, polygon)
        if (isBelong) {
            return Pair(index, polygon)
        }
    }
    return null
}

fun isSmallPage(pageIndex: Int) = pageIndex == 1 || pageIndex == 2

fun Int.toArabic(): String {
    val arabicDigits = mapOf(
        '0' to '٠', '1' to '١', '2' to '٢', '3' to '٣', '4' to '٤',
        '5' to '٥', '6' to '٦', '7' to '٧', '8' to '٨', '9' to '٩'
    )

    return this.toString().map { arabicDigits[it] ?: it }.joinToString("")
}
