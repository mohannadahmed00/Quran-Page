package com.giraffe.quranpage.data.datasource.remote.responses

import com.giraffe.quranpage.common.utils.Constants.ResponseAttributes.AYAH_NUMBER
import com.giraffe.quranpage.common.utils.Constants.ResponseAttributes.TAFSEER_ID
import com.giraffe.quranpage.common.utils.Constants.ResponseAttributes.TAFSEER_NAME
import com.google.gson.annotations.SerializedName

data class TafseerResponse(
    @SerializedName(TAFSEER_ID)
    val id: Int,
    @SerializedName(AYAH_NUMBER)
    val verseIndex: Int,
    @SerializedName(TAFSEER_NAME)
    val name: String,
    val text: String
)