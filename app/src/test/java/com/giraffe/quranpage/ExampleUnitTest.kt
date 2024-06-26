package com.giraffe.quranpage

import androidx.compose.ui.geometry.Offset
import com.giraffe.quranpage.remote.response.AyahResponse
import com.giraffe.quranpage.utils.isPointInsidePolygon
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    private val dummyAyahResponse = AyahResponse(
        ayahIndex = 1,
        endTime = 1000,
        pageUrl = "https://www.mp3quran.net/api/quran_pages_svg/075.svg",
        polygon = "10,54 20,52 30,12 40,15",
        startTime = 500,
        x = "50",
        y = "60"
    )

    private val polygonVertices = listOf(
        Offset(173.44f, 228.46f),
        Offset(0.0f, 228.46f),
        Offset(0.0f, 262.8f),
        Offset(196.36f, 262.8f),
        Offset(196.36f, 298.8f),
        Offset(343.0f, 298.8f),
        Offset(343.0f, 264.46f),
        Offset(173.44f, 264.46f),
        Offset(173.44f, 228.46f)
    )

    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun `get page index from url`() {
        assertEquals(75, dummyAyahResponse.getPageIndexFromUrl())
    }

    @Test
    fun `point is belong to the polygon`() {
        val pointToCheck = Offset(200.0f, 275.0f)
        val result = isPointInsidePolygon(pointToCheck, polygonVertices)
        assertEquals(true, result)
    }
    @Test
    fun `point is not belong to the polygon`() {
        val pointToCheck = Offset(200.0f, 245.0f)
        val result = isPointInsidePolygon(pointToCheck, polygonVertices)
        assertEquals(false, result)
    }


}