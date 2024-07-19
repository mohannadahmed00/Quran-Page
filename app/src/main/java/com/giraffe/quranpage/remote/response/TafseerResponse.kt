package com.giraffe.quranpage.remote.response

data class TafseerResponse(
    val ayah_number: Int,
    val tafseer_id: Int,
    val tafseer_name: String,
    val text: String
)