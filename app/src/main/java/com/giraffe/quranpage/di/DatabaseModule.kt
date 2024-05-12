package com.giraffe.quranpage.di

import android.content.Context
import androidx.room.Room
import com.giraffe.quranpage.local.LocalDataSource
import com.giraffe.quranpage.local.database.AppDao
import com.giraffe.quranpage.local.database.AppDatabase
import com.giraffe.quranpage.local.preferences.DataStorePreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideRoomDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "quran_page_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideContentDao(appDataBase: AppDatabase): AppDao {
        return appDataBase.getAppDao()
    }

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStorePreferences {
        return DataStorePreferences(context)
    }

    @Provides
    @Singleton
    fun provideLocalDataSource(@ApplicationContext context: Context,appDao: AppDao,dataStorePreferences: DataStorePreferences): LocalDataSource {
        return LocalDataSource(context,appDao,dataStorePreferences)
    }
}