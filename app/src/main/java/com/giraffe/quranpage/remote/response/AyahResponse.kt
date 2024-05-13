package com.giraffe.quranpage.remote.response

import androidx.compose.ui.geometry.Offset
import com.giraffe.quranpage.local.model.AyahModel
import com.giraffe.quranpage.utils.Constants
import com.google.gson.annotations.SerializedName

data class AyahResponse(
    @SerializedName(Constants.ResponseAttributes.AYAH)
    val ayahIndex: Int,
    @SerializedName(Constants.ResponseAttributes.END_TIME)
    val endTime: Int,
    @SerializedName(Constants.ResponseAttributes.PAGE)
    val pageUrl: String?,
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
        ayahIndex = ayahIndex,
        startTime = startTime,
        endTime = endTime,
        pageIndex = getPageIndexFromUrl(),
        polygon = convertPolygonToOffsets(),
        ayahIndexPosition = Offset(x?.toFloat() ?: 0f, y?.toFloat() ?: 0f),
    )

    fun getPageIndexFromUrl() =
        if (pageUrl.isNullOrBlank()) 0 else pageUrl.split("/quran_pages_svg/").last().split(".")
            .first().toInt()

    private fun convertPolygonToOffsets() =
        if (polygon.isNullOrBlank()) emptyList() else polygon.trim().split(" ").map {
            Offset(it.split(",")[0].toFloat(), it.split(",")[1].toFloat())
        }
}