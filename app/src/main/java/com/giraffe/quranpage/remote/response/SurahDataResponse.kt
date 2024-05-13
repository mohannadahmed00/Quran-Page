package com.giraffe.quranpage.remote.response

import com.giraffe.quranpage.local.model.SurahDataModel
import com.giraffe.quranpage.utils.Constants.ResponseAttributes
import com.google.gson.annotations.SerializedName

data class SurahDataResponse(
    @SerializedName(ResponseAttributes.END_PAGE)
    val endPage: Int,
    @SerializedName(ResponseAttributes.ID)
    val id: Int,
    @SerializedName(ResponseAttributes.MAKKIA)
    val makkia: Int,
    @SerializedName(ResponseAttributes.NAME)
    val name: String,
    @SerializedName(ResponseAttributes.START_PAGE)
    val startPage: Int,
    @SerializedName(ResponseAttributes.TYPE)
    val type: Int
) {
    fun toSurahDataModel() = SurahDataModel(
        id = id,
        endPage = endPage,
        startPage = startPage,
        makkia = makkia,
        name = name,
        type = type,
    )
}