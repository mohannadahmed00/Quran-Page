package com.giraffe.quranpage

import com.giraffe.quranpage.remote.response.AyahResponse
import com.giraffe.quranpage.remote.response.SurahResponse
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun `get page index from url`(){
        val dummyAyahResponse = AyahResponse(
            id = 1,
            ayah = 1,
            endTime = 1000,
            _page = "https://www.mp3quran.net/api/quran_pages_svg/075.svg",
            polygon = "10,54 20,52 30,12 40,15",
            startTime = 500,
            x = "50",
            y = "60"
        )
        assertEquals(75, dummyAyahResponse.pageIndex)
    }
}