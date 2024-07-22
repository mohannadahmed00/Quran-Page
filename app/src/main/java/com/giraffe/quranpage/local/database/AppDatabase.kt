package com.giraffe.quranpage.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.giraffe.quranpage.local.model.PageModel
import com.giraffe.quranpage.local.model.ReciterModel
import com.giraffe.quranpage.local.model.SurahDataModel
import com.giraffe.quranpage.utils.Converters

@Database(entities = [PageModel::class, SurahDataModel::class,ReciterModel::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getAppDao(): AppDao
    abstract fun getRecitersDao(): RecitersDao
}