package com.giraffe.quranpage.data.datasource.remote.responses

import com.giraffe.quranpage.common.utils.Constants.ResponseAttributes
import com.google.gson.annotations.SerializedName

data class ReciterResponse(
    val id: Int,
    val name: String,
    @SerializedName(ResponseAttributes.FOLDER_URL)
    val folderUrl: String,
    val rewaya: String
)