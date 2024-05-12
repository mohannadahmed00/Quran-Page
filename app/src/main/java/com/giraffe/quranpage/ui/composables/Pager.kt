package com.giraffe.quranpage.ui.composables

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.toSize
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.giraffe.quranpage.local.model.PageModel
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Pager(pages:List<PageModel>,changePageIndex:(pageIndex:Int)->Unit){
    val lazyListState = rememberLazyListState()
    LaunchedEffect(lazyListState) {
        snapshotFlow {
            lazyListState.firstVisibleItemIndex
        }
            .collectLatest {
                Log.d("messi", "onScroll: $it")
                changePageIndex(it+2)
            }
    }
    LazyRow(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxSize(),
        state = lazyListState,
        flingBehavior = rememberSnapFlingBehavior(lazyListState = lazyListState),
    ) {
        items(pages) { page ->
            ConstraintLayout(modifier = Modifier.fillParentMaxWidth()) {
                val (image, canvas) = createRefs()
                var imageSize by remember { mutableStateOf(Size.Zero) }
                SvgImage(
                    page.image,
                    modifier = Modifier
                        .constrainAs(image) {
                            linkTo(
                                top = parent.top,
                                bottom = parent.bottom,
                                start = parent.start,
                                end = parent.end
                            )
                        }
                        .fillParentMaxWidth()
                        .onSizeChanged { imageSize = it.toSize() },
                ) { clickedPoint ->
                    Log.d("messi", "clickedPoint: $clickedPoint")
                }
                Canvas(
                    modifier = Modifier.constrainAs(canvas) {
                        linkTo(
                            top = image.top,
                            bottom = image.bottom,
                            start = image.start,
                            end = image.end
                        )
                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                    }
                ) {
                    val path = Path()
                    var flag = true
                    /*state.ayahs[1].polygon.split(" ").forEach {
                        val x = it.split(",")[0].toFloat()
                        val y = it.split(",")[1].toFloat()
                        val point = Offset(x, y).normalizePoint(
                            imageSize.width,
                            imageSize.height,
                            page.pageIndex == 1 || page.pageIndex == 2
                        )
                        if (flag) {
                            path.moveTo(point.x, point.y)
                            flag = false
                        }
                        path.lineTo(point.x, point.y)
                    }*/
                    drawPath(path, Color.Yellow.copy(alpha = .3f))
                }
            }

        }
    }
}