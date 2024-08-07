package com.giraffe.quranpage.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.giraffe.quranpage.local.model.ReciterModel

@Dao
interface RecitersDao {
    @Query("SELECT * FROM reciters")
    fun getAllReciters(): List<ReciterModel>

    @Query("SELECT * FROM reciters WHERE id=:reciterId ")
    fun getReciter(reciterId:Int): ReciterModel

    @Query("SELECT COUNT(*) FROM reciters")
    fun getRecitersCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertReciter(reciterModel: ReciterModel): Long

    @Query("DELETE FROM pages")
    fun deleteAllReciters(): Int
}