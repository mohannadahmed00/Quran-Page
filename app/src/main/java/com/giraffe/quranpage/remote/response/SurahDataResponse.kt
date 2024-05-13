package com.giraffe.quranpage.remote.response

import com.giraffe.quranpage.local.model.SurahDataModel
import com.giraffe.quranpage.utils.Constants
import com.google.gson.annotations.SerializedName

data class SurahDataResponse(
    @SerializedName(Constants.ResponseAttributes.END_PAGE)
    val endPage: Int,
    @SerializedName(Constants.ResponseAttributes.ID)
    val id: Int,
    @SerializedName(Constants.ResponseAttributes.MAKKIA)
    val makkia: Int,
    @SerializedName(Constants.ResponseAttributes.NAME)
    val name: String,
    @SerializedName(Constants.ResponseAttributes.START_PAGE)
    val startPage: Int,
    @SerializedName(Constants.ResponseAttributes.TYPE)
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