package com.giraffe.quranpage.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.giraffe.quranpage.local.model.PageModel
import com.giraffe.quranpage.local.model.SurahDataModel

@Dao
interface AppDao {
    //============================================pages=============================================
    @Query("SELECT * FROM pages ORDER BY pageIndex ASC")
    fun getPages(): List<PageModel>

    @Query("SELECT COUNT(*) FROM pages")
    fun getPagesCount(): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertPage(page: PageModel): Long

    @Query("DELETE FROM pages")
    fun deletePages(): Int

    //=========================================surahes_data=========================================
    @Query("SELECT * FROM surahes_data WHERE id = :surahIndex")
    fun getSurahData(surahIndex: Int): SurahDataModel

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertSurahData(surahDataModel: SurahDataModel)

    @Query("SELECT COUNT(*) FROM surahes_data")
    fun getCountOfSurahesData(): Int
}