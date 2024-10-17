package com.giraffe.quranpage.common.di

import android.content.Context
import androidx.room.Room
import com.giraffe.quranpage.data.datasource.local.LocalDataSource
import com.giraffe.quranpage.data.datasource.local.LocalDataSourceImp
import com.giraffe.quranpage.data.datasource.local.database.AppDatabase
import com.giraffe.quranpage.data.datasource.local.database.RecitersDao
import com.giraffe.quranpage.data.datasource.local.preferences.DataStorePreferences
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
    fun provideRecitersDao(appDataBase: AppDatabase): RecitersDao {
        return appDataBase.getRecitersDao()
    }

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStorePreferences {
        return DataStorePreferences(context)
    }

    @Provides
    @Singleton
    fun provideLocalDataSource(
        @ApplicationContext context: Context,
        recitersDao: RecitersDao,
        dataStorePreferences: DataStorePreferences
    ): LocalDataSource {
        return LocalDataSourceImp(context, recitersDao, dataStorePreferences)
    }
}