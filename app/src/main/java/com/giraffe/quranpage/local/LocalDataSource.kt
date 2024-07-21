package com.giraffe.quranpage.local


import android.content.Context
import android.util.Log
import com.caverock.androidsvg.SVG
import com.giraffe.quranpage.local.database.AppDao
import com.giraffe.quranpage.local.model.AyahModel
import com.giraffe.quranpage.local.model.PageModel
import com.giraffe.quranpage.local.model.SurahDataModel
import com.giraffe.quranpage.local.model.SurahModel
import com.giraffe.quranpage.local.model.VerseModel
import com.giraffe.quranpage.local.preferences.DataStorePreferences
import com.giraffe.quranpage.utils.drawCircles
import com.giraffe.quranpage.utils.isSmallPage
import com.giraffe.quranpage.utils.renderSvgToBitmap
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class LocalDataSource(
    private val context: Context,
    private val appDao: AppDao,
    private val dataStorePreferences: DataStorePreferences,
) {
    fun storePageInDatabase(pageIndex: Int, svgData: String, ayahs: List<AyahModel>) {
        val bitmap = SVG.getFromString(svgData).renderSvgToBitmap(isSmallPage(pageIndex))
        val normalizedAyahs = ayahs.map { it.normalizeAyahIndexPosition(bitmap) }
        bitmap.drawCircles(context, normalizedAyahs, pageIndex)
        appDao.insertPage(PageModel(pageIndex, bitmap, normalizedAyahs))
    }

    fun getPagesCount() = appDao.getPagesCount()
    fun getPages() = appDao.getPages()
    fun storeSurahData(surahDataModel: SurahDataModel) = appDao.insertSurahData(surahDataModel)
    fun getSurahData(surahIndex: Int) = getSurahesData().toMutableList().firstOrNull { it.id == surahIndex }
    fun getCountOfSurahesData() = appDao.getCountOfSurahesData()

    //==============================================================================================
    fun getAllVerses(): List<VerseModel> = try {
        context.assets.open("quran_text.json").use { inputStream ->
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            val json = String(buffer)
            val verseListType = object : TypeToken<List<VerseModel>>() {}.type
            Gson().fromJson<List<VerseModel>>(json, verseListType)
            //.filter { it.pageIndex == pageIndex }
        }
    } catch (e: Exception) {
        Log.e(TAG, "Error loading page content: ${e.message}")
        emptyList()
    }

    fun getSurahesData(): List<SurahModel> = try {
        context.assets.open("surahes_data.json").use { inputStream ->
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            val json = String(buffer)
            val verseListType = object : TypeToken<List<SurahModel>>() {}.type
            Gson().fromJson(json, verseListType)
        }
    } catch (e: Exception) {
        Log.e(TAG, "Error loading page content: ${e.message}")
        emptyList()
    }

    companion object {
        private const val TAG = "LocalDataSource"
    }
}

