package com.giraffe.quranpage.remote.response

import androidx.room.PrimaryKey
import com.giraffe.quranpage.local.model.AyahModel
import com.giraffe.quranpage.utils.Constants
import com.google.gson.annotations.SerializedName

data class AyahResponse(
    @PrimaryKey(true)
    val id: Int = 0,
    @SerializedName(Constants.ResponseAttributes.AYAH)
    val ayah: Int,
    @SerializedName(Constants.ResponseAttributes.END_TIME)
    val endTime: Int,
    @SerializedName(Constants.ResponseAttributes.PAGE)
    val page: String?,
    @SerializedName(Constants.ResponseAttributes.POLYGON)
    val polygon: String?,
    @SerializedName(Constants.ResponseAttributes.START_TIME)
    val startTime: Int,
    @SerializedName(Constants.ResponseAttributes.X)
    val x: String?,
    @SerializedName(Constants.ResponseAttributes.Y)
    val y: String?,
) {
    fun toAyahModel() = AyahModel(
        ayah = ayah,
        endTime = endTime,
        pageIndex = getPageIndexFromUrl() ?: -1,
        polygon = polygon?.trim() ?: "",
        startTime = startTime,
        x = x?.toFloat() ?: 0f,
        y = y?.toFloat() ?: 0f,
    )

    private fun getPageIndexFromUrl() =
        page?.split("/quran_pages_svg/")?.last()?.split(".")?.first()?.toInt()
}