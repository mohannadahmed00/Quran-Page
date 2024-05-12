package com.giraffe.quranpage.local


import android.content.Context
import androidx.compose.ui.graphics.Path
import com.giraffe.quranpage.local.database.AppDao
import com.giraffe.quranpage.local.model.AyahModel
import com.giraffe.quranpage.local.model.PageModel
import com.giraffe.quranpage.local.preferences.DataStorePreferences
import com.giraffe.quranpage.utils.drawCircle
import com.giraffe.quranpage.utils.loadSvgBitmap

class LocalDataSource(
    private val context: Context,
    private val appDao: AppDao,
    private val dataStorePreferences: DataStorePreferences,
) {
    fun storePageInDatabase(pageIndex: Int, svgData: String, ayahs: List<AyahModel>): Long {
        val bitmap = svgData.loadSvgBitmap(pageIndex == 1 || pageIndex == 2)
        ayahs.forEach {
            if (it.x != 0f || it.y != 0f) bitmap.drawCircle(context, pageIndex, it.x, it.y)
        }
        return appDao.insertPage(PageModel(pageIndex, bitmap))
    }
    fun getPagesCount() = appDao.getPagesCount()
    fun getAyahsCount() = appDao.getAyahsCount()
    fun getPages() = appDao.getPages()
    fun deletePages() = appDao.deletePages()
    fun storeAyah(ayahModel: AyahModel) = appDao.insertAyah(ayahModel)
    fun getAyahs(pageIndex: Int) = appDao.getAyahsOfPage(pageIndex)
    fun getAyahs() = appDao.getAyahs()
}

