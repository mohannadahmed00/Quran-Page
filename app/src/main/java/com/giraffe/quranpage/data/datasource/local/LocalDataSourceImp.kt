package com.giraffe.quranpage.data.datasource.local


import android.content.Context
import com.giraffe.quranpage.common.utils.Constants
import com.giraffe.quranpage.common.utils.Constants.Keys.BOOKMARKED_VERSE
import com.giraffe.quranpage.common.utils.Constants.SURAHES_DATA_JSON
import com.giraffe.quranpage.common.utils.Constants.VERSES_DATA_JSON
import com.giraffe.quranpage.data.datasource.local.database.RecitersDao
import com.giraffe.quranpage.data.datasource.local.models.ReciterModel
import com.giraffe.quranpage.data.datasource.local.models.SurahDataModel
import com.giraffe.quranpage.data.datasource.local.models.VerseModel
import com.giraffe.quranpage.data.datasource.local.preferences.DataStorePreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.inject.Inject

class LocalDataSourceImp @Inject constructor(
    private val context: Context,
    private val recitersDao: RecitersDao,
    private val dataStorePreferences: DataStorePreferences,
) : LocalDataSource {
    override suspend fun bookmarkVerse(verseModel: VerseModel) =
        dataStorePreferences.save(BOOKMARKED_VERSE, Gson().toJson(verseModel))

    override suspend fun getBookmarkedVerse() =
        dataStorePreferences.readString(BOOKMARKED_VERSE)?.let {
            Gson().fromJson(it, VerseModel::class.java)
        }

    override suspend fun removeBookmarkedVerse() = dataStorePreferences.removeInt(BOOKMARKED_VERSE)
    override suspend fun saveLastPageIndex(pageIndex: Int) =
        dataStorePreferences.save(Constants.Keys.LAST_PAGE_INDEX, pageIndex)

    override suspend fun getLastPageIndex() =
        dataStorePreferences.readInt(Constants.Keys.LAST_PAGE_INDEX)

    override fun storeReciter(reciterModel: ReciterModel) = recitersDao.insertReciter(reciterModel)
    override fun getAllReciters() = recitersDao.getAllReciters()
    override fun getReciter(reciterId: Int) = recitersDao.getReciter(reciterId)
    override fun getRecitersCount() = recitersDao.getRecitersCount()

    override fun getAllVerses(): List<VerseModel> = try {
        context.assets.open(VERSES_DATA_JSON).use { inputStream ->
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            val json = String(buffer)
            val verseListType = object : TypeToken<List<VerseModel>>() {}.type
            Gson().fromJson(json, verseListType)
        }
    } catch (e: Exception) {
        emptyList()
    }

    override fun getSurahesData(): List<SurahDataModel> = try {
        context.assets.open(SURAHES_DATA_JSON).use { inputStream ->
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            val json = String(buffer)
            val verseListType = object : TypeToken<List<SurahDataModel>>() {}.type
            Gson().fromJson(json, verseListType)
        }
    } catch (e: Exception) {
        emptyList()
    }
}

