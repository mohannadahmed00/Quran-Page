package com.giraffe.quranpage.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import androidx.compose.ui.geometry.Offset
import androidx.core.content.ContextCompat
import com.caverock.androidsvg.SVG
import com.giraffe.quranpage.R

fun Int.toThreeDigits(): String {
    var intStr = this.toString()
    while (intStr.length < 3) {
        intStr = "0".plus(intStr)
    }
    return intStr
}

fun Offset.normalizePoint(newWidth: Int, newHeight: Int, isSmall: Boolean) =
    Offset(x * (newWidth / if (isSmall) 235 else 345), y * (newHeight / if (isSmall) 235 else 550))

fun Offset.normalizePoint(newWidth: Float, newHeight: Float, isSmall: Boolean) =
    Offset(x * (newWidth / if (isSmall) 235 else 345), y * (newHeight / if (isSmall) 235 else 550))

fun Bitmap.drawCircle(context: Context, pageIndex: Int, x: Float, y: Float) {
    val canvas = Canvas(this)
    val point = Offset(x, y).normalizePoint(
        this.width,
        this.height,
        pageIndex == 1 || pageIndex == 2
    )
    val drawableId = R.drawable.ayah_end
    val drawable = ContextCompat.getDrawable(context, drawableId)
    drawable?.setTint(ContextCompat.getColor(context, R.color.teal_700))
    val width = if (pageIndex == 1 || pageIndex == 2) 35 else 55
    val height = if (pageIndex == 1 || pageIndex == 2) 40 else 63
    val left = point.x - (width / 2)
    val top = point.y - (height / 2)
    val right = left + width
    val bottom = top + height
    val destinationRect = Rect(left.toInt(), top.toInt(), right.toInt(), bottom.toInt())
    drawable?.bounds = destinationRect
    drawable?.draw(canvas)
    canvas.save()
}

fun String.loadSvgBitmap(isSmall: Boolean) = SVG.getFromString(this).renderSvgToBitmap(isSmall)

fun SVG.renderSvgToBitmap(isSmall: Boolean = false): Bitmap {
    val bitmap = Bitmap.createBitmap(
        if (isSmall) 235 * 3 else 345 * 3,
        if (isSmall) 235 * 3 else 550 * 3,
        Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    this.renderToCanvas(canvas)
    return bitmap
}
