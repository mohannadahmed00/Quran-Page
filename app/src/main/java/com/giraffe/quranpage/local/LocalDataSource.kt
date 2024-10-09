package com.giraffe.quranpage.local


import android.content.Context
import android.util.Log
import com.caverock.androidsvg.SVG
import com.giraffe.quranpage.local.database.AppDao
import com.giraffe.quranpage.local.database.RecitersDao
import com.giraffe.quranpage.local.model.AyahModel
import com.giraffe.quranpage.local.model.PageModel
import com.giraffe.quranpage.local.model.ReciterModel
import com.giraffe.quranpage.local.model.SurahDataModel
import com.giraffe.quranpage.local.model.SurahModel
import com.giraffe.quranpage.local.model.VerseModel
import com.giraffe.quranpage.local.preferences.DataStorePreferences
import com.giraffe.quranpage.utils.Constants
import com.giraffe.quranpage.utils.Constants.Keys.BOOKMARKED_VERSE
import com.giraffe.quranpage.utils.drawCircles
import com.giraffe.quranpage.utils.isSmallPage
import com.giraffe.quranpage.utils.renderSvgToBitmap
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class LocalDataSource(
    private val context: Context,
    private val appDao: AppDao,
    private val recitersDao: RecitersDao,
    private val dataStorePreferences: DataStorePreferences,
) {
    fun storePageInDatabase(pageIndex: Int, svgData: String, ayahs: List<AyahModel>) {
        val bitmap = SVG.getFromString(svgData).renderSvgToBitmap(isSmallPage(pageIndex))
        val normalizedAyahs = ayahs.map { it.normalizeAyahIndexPosition(bitmap) }
        bitmap.drawCircles(context, normalizedAyahs, pageIndex)
        appDao.insertPage(PageModel(pageIndex, bitmap, normalizedAyahs))
    }

    suspend fun bookmarkVerse(verseModel: VerseModel?) {
        verseModel?.let { dataStorePreferences.save(BOOKMARKED_VERSE, Gson().toJson(verseModel)) }
            ?: dataStorePreferences.save(BOOKMARKED_VERSE, "")


    }

    suspend fun getBookmarkedVerse() =
        dataStorePreferences.readString(BOOKMARKED_VERSE)?.let {
            if (it.isNotEmpty()){
                Gson().fromJson(it, VerseModel::class.java)
            }else{
                null
            }
        }


    suspend fun saveLastPageIndex(pageIndex: Int) {
        Log.d(TAG, "saveLastPageIndex: $pageIndex")
        dataStorePreferences.save(Constants.Keys.LAST_PAGE_INDEX, pageIndex)
    }

    suspend fun getLastPageIndex() = dataStorePreferences.readInt(Constants.Keys.LAST_PAGE_INDEX)

    fun getPagesCount() = appDao.getPagesCount()
    fun getPages() = appDao.getPages()
    fun storeSurahData(surahDataModel: SurahDataModel) = appDao.insertSurahData(surahDataModel)
    fun getSurahData(surahIndex: Int) =
        getSurahesData().toMutableList().firstOrNull { it.id == surahIndex }

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
        emptyList()
    }


    fun storeReciter(reciterModel: ReciterModel) = recitersDao.insertReciter(reciterModel)
    fun getAllReciters() = recitersDao.getAllReciters()
    fun getRecitersCount() = recitersDao.getRecitersCount()
    fun getReciter(reciterId: Int) = recitersDao.getReciter(reciterId)

    companion object {
        private const val TAG = "LocalDataSource"
    }
}

