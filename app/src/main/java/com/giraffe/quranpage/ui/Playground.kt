package com.giraffe.quranpage.ui

import android.graphics.Bitmap
import android.graphics.drawable.PictureDrawable
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.graphics.drawable.toBitmap
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.caverock.androidsvg.SVG
import com.giraffe.quranpage.R
import com.giraffe.quranpage.utils.normalizePoint
import com.google.accompanist.drawablepainter.rememberDrawablePainter



@Composable
fun PlaygroundScreenContent(
    state: HomeState = HomeState(),
    nextAyah: () -> Unit,
    previousAyah: () -> Unit
) {
    /*val context = LocalContext.current
    ConstraintLayout {
        val (text, image, canvas, nextBtn, previousBtn) = createRefs()
        if (state.surahResponse != null) Canvas(
            modifier = Modifier
                .constrainAs(canvas) {
                    top.linkTo(image.top)
                    bottom.linkTo(image.bottom)
                    start.linkTo(image.start)
                    end.linkTo(image.end)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
        ) {
            val imageBitmap = R.drawable.ayah_end.convertDrawableResToImageBitmap(context)
            for (i in state.surahResponse.indices){
                if (i!=0){
                    val ayahNumPoint = normalizePoint(
                        state.surahResponse[i].x.toFloat().dp.toPx(),
                        state.surahResponse[i].y.toFloat().dp.toPx(),
                        45f.dp,
                        45f.dp,
                        //imageWidth,
                        //imageHeight,
                        true
                    )
                    drawImage(
                        imageBitmap,
                        ayahNumPoint.copy(x = ayahNumPoint.x - 17, y = ayahNumPoint.y - 20),
                        colorFilter = ColorFilter.tint(Color(66, 140, 59))
                    )
                }

            }
        }
    }*/
}