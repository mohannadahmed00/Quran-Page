package com.giraffe.quranpage.ui.composables

import android.content.Context
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.giraffe.quranpage.R
import com.giraffe.quranpage.ui.HomeState
import com.giraffe.quranpage.utils.normalizePoint

@Composable
fun ImageWithPolygon(
    modifier: Modifier,
    polygonPoints: String,
    newWidth: Dp,
    newHeight: Dp,
    state: HomeState,
    context: Context
) {
    /*Canvas(
        modifier = modifier
    ) {
        val path = Path()
        var f = true
        polygonPoints.split(" ").map {
            val x = it.split(",")[0].toFloat().dp.toPx()
            val y = it.split(",")[1].toFloat().dp.toPx()
            val newPoint = normalizePoint(x, y, newWidth, newHeight)
            if (f) {
                path.moveTo(newPoint.x, newPoint.y)
                f = false
            }
            path.lineTo(newPoint.x, newPoint.y)
            newPoint
        }
        drawPath(path, Color.Yellow.copy(alpha = .3f))
        val imageBitmap = R.drawable.ayah_end.convertDrawableResToImageBitmap(context)
        val ayahNumPoint = normalizePoint(
            state.surahResponse?.get(125)?.x?.toFloat()?.minus(imageBitmap.width / 4)?.dp?.toPx()
                ?: 0f,
            state.surahResponse?.get(125)?.y?.toFloat()?.minus(imageBitmap.height / 4)?.dp?.toPx()
                ?: 0f,
            newWidth,
            newHeight
        )
        drawImage(
            imageBitmap,
            ayahNumPoint,
            colorFilter = ColorFilter.tint(Color(66, 140, 59))
        )
    }*/
}
