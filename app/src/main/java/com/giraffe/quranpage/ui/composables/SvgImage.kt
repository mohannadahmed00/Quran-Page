package com.giraffe.quranpage.ui.composables

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.toSize

@Composable
fun SvgImage(modifier: Modifier = Modifier, bitmap: Bitmap, onClicked: (point: Offset) -> Unit) {
    var imageSize by remember { mutableStateOf(Size.Zero) }
    Image(
        modifier = modifier
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