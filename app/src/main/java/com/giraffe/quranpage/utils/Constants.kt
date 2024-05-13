package com.giraffe.quranpage.utils

object Constants {
    const val BASE_URL = "https://mp3quran.net/api/v3/"
    const val PAGES_URL = "https://www.mp3quran.net/api/quran_pages_svg/000.svg"
    //const val PAGES_URL = "https://www.maknoon.com/quran/hafs/000.svgz"

    object ResponseAttributes {
        const val AYAH = "ayah"
        const val END_TIME = "end_time"
        const val PAGE = "page"
        const val POLYGON = "polygon"
        const val START_TIME = "start_time"
        const val X = "x"
        const val Y = "y"

        const val SUWAR = "suwar"
        const val END_PAGE = "end_page"
        const val ID = "id"
        const val MAKKIA = "makkia"
        const val NAME = "name"
        const val START_PAGE = "start_page"
        const val TYPE = "type"

    }

    object EndPoints {
        const val AYAT_TIMING = "ayat_timing"
        const val SUWAR = "suwar"
    }

    object QueryParameters {
        const val SURAH = "surah"
        const val READ = "read"
    }

    object DatabaseTables {
        const val PAGES = "pages"
        const val SURAHES_DATA = "surahes_data"
    }

    object PageDimensions {
        const val SMALL_WIDTH = 235
        const val SMALL_HEIGHT = 235
        const val NORMAL_WIDTH = 345
        const val NORMAL_HEIGHT = 550
        /*const val SMALL_WIDTH = 241
        const val SMALL_HEIGHT = 324
        const val NORMAL_WIDTH = 293
        const val NORMAL_HEIGHT = 485*/
    }
}