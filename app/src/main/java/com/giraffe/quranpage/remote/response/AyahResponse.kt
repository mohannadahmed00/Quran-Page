package com.giraffe.quranpage.remote.response

import androidx.compose.ui.geometry.Offset
import com.giraffe.quranpage.local.model.AyahModel
import com.google.gson.annotations.SerializedName
import com.giraffe.quranpage.utils.Constants.ResponseAttributes

data class AyahResponse(
    @SerializedName(ResponseAttributes.AYAH)
    val ayahIndex: Int,
    @SerializedName(ResponseAttributes.END_TIME)
    val endTime: Int,
    @SerializedName(ResponseAttributes.PAGE)
    val pageUrl: String?,
    @SerializedName(ResponseAttributes.POLYGON)
    val polygon: String?,
    @SerializedName(ResponseAttributes.START_TIME)
    val startTime: Int,
    @SerializedName(ResponseAttributes.X)
    val x: String?,
    @SerializedName(ResponseAttributes.Y)
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
        if (polygon.isNullOrBlank()) emptyList() else polygon.trim().split(" ")
            .filter { it.isNotBlank() }.map {
                Offset(it.trim().split(",")[0].toFloat(), it.trim().split(",")[1].toFloat())
            }
}