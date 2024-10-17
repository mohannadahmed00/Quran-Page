package com.giraffe.quranpage.common.utils

object Constants {
    const val TAFSEER_BASE_URL = "http://api.quran-tafseer.com/"
    const val RECITERS_BASE_URL = "https://mp3quran.net/api/v3/"
    const val VERSES_DATA_JSON = "quran_text.json"
    const val SURAHES_DATA_JSON = "surahes_data.json"

    object ResponseAttributes {
        const val AYAH = "ayah"
        const val END_TIME = "end_time"
        const val PAGE = "page"
        const val POLYGON = "polygon"
        const val START_TIME = "start_time"
        const val X = "x"
        const val Y = "y"
        const val FOLDER_URL = "folder_url"
        const val AYAH_NUMBER = "ayah_number"
        const val TAFSEER_ID = "tafseer_id"
        const val TAFSEER_NAME = "tafseer_name"

    }

    object EndPoints {
        const val READS = "reads"
        const val AYAT_TIMING = "ayat_timing"
        const val TAFSIR = "tafseer"
    }

    object QueryParameters {
        const val SURAH = "surah"
        const val READ = "read"
    }

    object DatabaseTables {
        const val RECITERS = "RECITERS"
        const val PAGES = "pages"
        const val SURAHES_DATA = "surahes_data"
    }


    object PathSegments {
        const val TAFSEER_ID = "TAFSEER_ID"
        const val SURAH_INDEX = "SURAH_INDEX"
        const val AYAH_INDEX = "AYAH_INDEX"
    }

    object Keys {
        const val RECITER_ID = "RECITER_ID"
        const val RECITER_NAME = "RECITER_NAME"
        const val SURAH_ID = "SURAH_ID"
        const val SURAH_NAME = "SURAH_NAME"
        const val URL = "URL"
        const val NOTIFICATION_ID = "NOTIFICATION_ID"
        const val LAST_PAGE_INDEX = "LAST_PAGE_INDEX"
        const val BOOKMARKED_VERSE = "BOOKMARKED_VERSE"
    }

    object Actions {
        const val START_DOWNLOAD = "START_DOWNLOAD"
        const val CANCEL_DOWNLOAD = "CANCEL_DOWNLOAD"
        const val PLAY = "PLAY"
        const val PAUSE = "PAUSE"
        const val STOP = "STOP"
        const val NEXT = "NEXT"
        const val PREVIOUS = "PREVIOUS"
        const val RELEASE = "RELEASE"
    }
}