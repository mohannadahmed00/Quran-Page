package com.giraffe.quranpage.di

import com.giraffe.quranpage.remote.RemoteDataSource
import com.giraffe.quranpage.remote.api.ApiServices
import com.giraffe.quranpage.utils.Constants
import com.giraffe.quranpage.remote.downloader.PageDownloader
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideHttpClient() = OkHttpClient.Builder().apply {
        addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .build()
            chain.proceed(request)
        }
    }

    @Provides
    @Singleton
    fun provideGsonBuilder(): Gson = GsonBuilder()
        .setLenient()
        .create()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient.Builder, gson: Gson): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient.build())
            .build()


    @Provides
    @Singleton
    fun provideApiServices(retrofit: Retrofit): ApiServices {
        return retrofit.create(ApiServices::class.java)
    }

    @Provides
    @Singleton
    fun provideDownloaderPage(httpClient: OkHttpClient.Builder): PageDownloader {
        return PageDownloader(httpClient)
    }


    @Provides
    @Singleton
    fun provideRemoteDataSource(
        apiServices: ApiServices,
        pageDownloader: PageDownloader
    ): RemoteDataSource {
        return RemoteDataSource(apiServices, pageDownloader)
    }
}


