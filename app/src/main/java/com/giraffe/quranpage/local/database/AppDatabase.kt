package com.giraffe.quranpage.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.giraffe.quranpage.local.model.AyahModel
import com.giraffe.quranpage.local.model.PageModel
import com.giraffe.quranpage.utils.Converters

@Database(entities = [AyahModel::class,PageModel::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getAppDao(): AppDao
}