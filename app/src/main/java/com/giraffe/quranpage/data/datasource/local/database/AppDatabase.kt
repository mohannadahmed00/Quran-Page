package com.giraffe.quranpage.data.datasource.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.giraffe.quranpage.data.datasource.local.models.ReciterModel

@Database(entities = [ReciterModel::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getRecitersDao(): RecitersDao
}