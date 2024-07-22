package com.giraffe.quranpage.remote.response

import com.giraffe.quranpage.local.model.ReciterModel
import com.giraffe.quranpage.utils.Constants.ResponseAttributes
import com.google.gson.annotations.SerializedName

data class ReciterResponse(
    val id: Int,
    val name: String,
    @SerializedName(ResponseAttributes.FOLDER_URL)
    val folderUrl: String,
    val rewaya:String
){
    fun toModel()=ReciterModel(
        this.id,
        this.name,
        this.folderUrl,
        this.rewaya
    )
}