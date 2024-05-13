package com.giraffe.quranpage.remote.response

import com.giraffe.quranpage.utils.Constants.ResponseAttributes
import com.google.gson.annotations.SerializedName

data class QuranDataResponse(
    @SerializedName(ResponseAttributes.SUWAR)
    val surahesData: List<SurahDataResponse>
)

