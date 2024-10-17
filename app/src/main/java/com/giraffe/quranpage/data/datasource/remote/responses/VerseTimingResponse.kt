package com.giraffe.quranpage.data.datasource.remote.responses

import com.giraffe.quranpage.common.utils.Constants.ResponseAttributes.AYAH
import com.giraffe.quranpage.common.utils.Constants.ResponseAttributes.END_TIME
import com.giraffe.quranpage.common.utils.Constants.ResponseAttributes.PAGE
import com.giraffe.quranpage.common.utils.Constants.ResponseAttributes.POLYGON
import com.giraffe.quranpage.common.utils.Constants.ResponseAttributes.START_TIME
import com.giraffe.quranpage.common.utils.Constants.ResponseAttributes.X
import com.giraffe.quranpage.common.utils.Constants.ResponseAttributes.Y
import com.google.gson.annotations.SerializedName

data class VerseTimingResponse(
    @SerializedName(AYAH)
    val verseIndex: Int,
    @SerializedName(START_TIME)
    val startTime: Int,
    @SerializedName(END_TIME)
    val endTime: Int,
    @SerializedName(PAGE)
    val pageUrl: String?,
    @SerializedName(POLYGON)
    val polygon: String?,
    @SerializedName(X)
    val x: String?,
    @SerializedName(Y)
    val y: String?,
)