package com.giraffe.quranpage.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import com.giraffe.quranpage.local.model.AyahModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.ByteArrayOutputStream

class Converters {
    @TypeConverter
    fun fromBitmap(bitmap: Bitmap): ByteArray {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return outputStream.toByteArray()
    }

    @TypeConverter
    fun toBitmap(byteArray: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }

    @TypeConverter
    fun fromAyahs(ayahs: List<AyahModel>): String {
        return Gson().toJson(ayahs)
    }

    @TypeConverter
    fun toAyahs(json: String): List<AyahModel> {
        return try {
            Gson().fromJson<List<AyahModel>>(json)
        } catch (e: Exception) {
            arrayListOf()
        }
    }

    private inline fun <reified T> Gson.fromJson(json: String): T =
        fromJson(json, object : TypeToken<T>() {}.type)
}