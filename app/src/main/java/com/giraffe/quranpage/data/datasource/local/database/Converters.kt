package com.giraffe.quranpage.data.datasource.local.database

import androidx.room.TypeConverter
import com.giraffe.quranpage.data.datasource.local.models.SurahAudioDataModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromSurahesAudioDataModel(surahesAudioModel: List<SurahAudioDataModel>): String {
        return Gson().toJson(surahesAudioModel)
    }

    @TypeConverter
    fun toSurahesAudioDataModel(json: String): List<SurahAudioDataModel> {
        return try {
            Gson().fromJson<List<SurahAudioDataModel>>(json)
        } catch (e: Exception) {
            arrayListOf()
        }
    }

    private inline fun <reified T> Gson.fromJson(json: String): T =
        fromJson(json, object : TypeToken<T>() {}.type)
}