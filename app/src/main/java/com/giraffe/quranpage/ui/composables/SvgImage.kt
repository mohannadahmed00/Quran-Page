package com.giraffe.quranpage.ui.composables

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.toSize

@Composable
fun SvgImage(bitmap: Bitmap, modifier: Modifier = Modifier, onClicked: (point: Offset) -> Unit) {
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }
    var imageSize by remember { mutableStateOf(Size.Zero) }
    Image(
        modifier = modifier
            .background(Color.Green.copy(alpha = 0.2f))
            .onSizeChanged { imageSize = it.toSize() }
            .pointerInput(Unit) {
                detectTapGestures {
                    onClicked(Offset(it.x, it.y))
                }
            },
        painter = BitmapPainter(bitmap.asImageBitmap()),
        contentDescription = "",
    )
}