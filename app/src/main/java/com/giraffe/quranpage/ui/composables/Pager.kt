package com.giraffe.quranpage.ui.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.toSize
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.giraffe.quranpage.local.model.PageModel
import com.giraffe.quranpage.utils.getSelectedPath

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Pager(
    pages: List<PageModel>,
    selectedPageIndex: Int,
    selectedPolygon: List<Offset>,
    selectAyah: (ayahIndex: Int, polygon: List<Offset>, pageIndex: Int) -> Unit
) {
    val lazyListState = rememberLazyListState()
    LazyRow(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxSize(),
        state = lazyListState,
        flingBehavior = rememberSnapFlingBehavior(lazyListState = lazyListState),
    ) {
        items(pages) { page ->
            ConstraintLayout(modifier = Modifier.fillParentMaxSize()) {
                val (image, canvas) = createRefs()
                var imageSize by remember { mutableStateOf(Size.Zero) }
                SvgImage(
                    bitmap = page.image,
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
                ) {
                    getSelectedPath(it, page.ayahs, imageSize)?.let { selectedPath ->
                        selectAyah(selectedPath.first, selectedPath.second, page.pageIndex)
                    }
                }
                if (page.pageIndex == selectedPageIndex) Polygon(
                    polygon = selectedPolygon,
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
                )
            }
        }
    }
}