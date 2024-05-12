package com.giraffe.quranpage.local.database

import androidx.compose.ui.graphics.Path
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.giraffe.quranpage.local.model.AyahModel
import com.giraffe.quranpage.local.model.PageModel

@Dao
interface AppDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertPage(page: PageModel): Long

    @Query("SELECT COUNT(*) FROM pages")
    fun getPagesCount(): Int

    @Query("SELECT * FROM pages ORDER BY pageIndex ASC")
    fun getPages(): List<PageModel>

    @Query("DELETE FROM pages")
    fun deletePages(): Int

    @Query("SELECT * FROM ayahs ORDER BY ayah ASC")
    fun getAyahs(): List<AyahModel>

    @Query("SELECT * FROM ayahs WHERE pageIndex = :pageIndex ORDER BY ayah ASC")
    fun getAyahsOfPage(pageIndex: Int): List<AyahModel>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAyah(ayahModel: AyahModel)

    @Query("SELECT COUNT(*) FROM ayahs")
    fun getAyahsCount(): Int
}