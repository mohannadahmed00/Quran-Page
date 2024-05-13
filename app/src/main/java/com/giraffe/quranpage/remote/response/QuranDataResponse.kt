package com.giraffe.quranpage.remote.response

import com.giraffe.quranpage.utils.Constants
import com.google.gson.annotations.SerializedName

data class QuranDataResponse(
    @SerializedName(Constants.ResponseAttributes.SUWAR)
    val surahesData: List<SurahDataResponse>
)

