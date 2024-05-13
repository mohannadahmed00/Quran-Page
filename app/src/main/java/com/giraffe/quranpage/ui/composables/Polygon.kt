package com.giraffe.quranpage.ui.composables

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path

@Composable
fun Polygon(
    modifier: Modifier = Modifier,
    polygon: List<Offset>
) {
    Canvas(
        modifier = modifier
    ) {
        val path = Path()
        var flag = true
        polygon.forEach { point ->
            if (flag) {
                path.moveTo(point.x, point.y)
                flag = false
            }
            path.lineTo(point.x, point.y)
        }
        drawPath(path, Color.Yellow.copy(alpha = .3f))
    }
}