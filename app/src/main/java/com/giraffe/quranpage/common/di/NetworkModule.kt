package com.giraffe.quranpage.common.di

import android.content.Context
import com.giraffe.quranpage.common.utils.Constants.RECITERS_BASE_URL
import com.giraffe.quranpage.common.utils.Constants.TAFSEER_BASE_URL
import com.giraffe.quranpage.data.datasource.remote.RemoteDataSource
import com.giraffe.quranpage.data.datasource.remote.RemoteDataSourceImp
import com.giraffe.quranpage.data.datasource.remote.api.AudioApiService
import com.giraffe.quranpage.data.datasource.remote.api.RecitersApiServices
import com.giraffe.quranpage.data.datasource.remote.api.TafseerApiServices
import com.giraffe.quranpage.data.datasource.remote.downloader.AudioDownloader
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
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
    @Tafseer
    fun provideTafseerRetrofit(okHttpClient: OkHttpClient.Builder, gson: Gson): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(TAFSEER_BASE_URL)
            .client(okHttpClient.build())
            .build()

    @Provides
    @Singleton
    @Reciters
    fun provideRecitersRetrofit(okHttpClient: OkHttpClient.Builder, gson: Gson): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(RECITERS_BASE_URL)
            .client(okHttpClient.build())
            .build()

    @Provides
    @Singleton
    @Audio
    fun provideAudioRetrofit(gson: Gson): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://example.com/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(
                OkHttpClient.Builder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .build()
            )
            .build()


    @Provides
    @Singleton
    fun provideTafseerApiServices(@Tafseer retrofit: Retrofit): TafseerApiServices {
        return retrofit.create(TafseerApiServices::class.java)
    }

    @Provides
    @Singleton
    fun provideRecitersApiServices(@Reciters retrofit: Retrofit): RecitersApiServices {
        return retrofit.create(RecitersApiServices::class.java)
    }

    @Provides
    @Singleton
    fun provideAudioApiServices(@Audio retrofit: Retrofit): AudioApiService {
        return retrofit.create(AudioApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideAudioDownloader(
        @ApplicationContext context: Context,
        audioApiService: AudioApiService
    ): AudioDownloader {
        return AudioDownloader(context, audioApiService)
    }


    @Provides
    @Singleton
    fun provideRemoteDataSource(
        tafseerApiServices: TafseerApiServices,
        recitersApiServices: RecitersApiServices,
    ): RemoteDataSource {
        return RemoteDataSourceImp(tafseerApiServices, recitersApiServices)
    }
}


@Qualifier
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER,
    AnnotationTarget.VALUE_PARAMETER,
    AnnotationTarget.FIELD
)
annotation class Tafseer

@Qualifier
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER,
    AnnotationTarget.VALUE_PARAMETER,
    AnnotationTarget.FIELD
)
annotation class Reciters

@Qualifier
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER,
    AnnotationTarget.VALUE_PARAMETER,
    AnnotationTarget.FIELD
)
annotation class Audio

