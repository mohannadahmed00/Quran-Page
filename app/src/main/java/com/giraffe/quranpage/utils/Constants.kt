package com.giraffe.quranpage.utils

object Constants {
    const val BASE_URL = "https://mp3quran.net/api/v3/"
    const val PAGES_URL = "https://www.mp3quran.net/api/quran_pages_svg/000.svg"
    //const val PAGES_URL = "https://raw.githubusercontent.com/batoulapps/quran-svg/main/svg/000.svg"
    //const val PAGES_URL = "https://raw.githubusercontent.com/salahamassi/Quran-svg-mobile/main/output/000.svg"//issue in page 007
    //const val PAGES_URL = "https://www.maknoon.com/quran/hafs/$pageIndex.svgz"

    object ResponseAttributes {
        const val AYAH = "ayah"
        const val END_TIME = "end_time"
        const val PAGE = "page"
        const val POLYGON = "polygon"
        const val START_TIME = "start_time"
        const val X = "x"
        const val Y = "y"
    }

    object EndPoints {
        const val AYAT_TIMING = "ayat_timing"
    }

    object DatabaseTables {
        const val PAGES = "pages"
        const val AYAHS = "ayahs"
    }
    object PreferenceKeys{
        const val SMALL_WIDTH = "small_width"
        const val SMALL_HEIGHT = "small_height"
        const val NORMAL_WIDTH = "normal_width"
        const val NORMAL_HEIGHT = "normal_height"
    }

}