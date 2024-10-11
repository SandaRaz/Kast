package com.ssw.kast.network.module

import android.content.Context
import com.google.gson.GsonBuilder
import com.ssw.kast.model.persistence.PreferencesManager
import com.ssw.kast.model.serializer.DtoTS
import com.ssw.kast.model.serializer.LDTtoDT
import com.ssw.kast.model.serializer.LDtoDT
import com.ssw.kast.network.service.PlaylistService
import com.ssw.kast.network.service.SearchService
import com.ssw.kast.network.service.SongService
import com.ssw.kast.network.service.UserService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.threeten.bp.Duration
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private val client = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .connectTimeout(120, TimeUnit.SECONDS)
        .readTimeout(120, TimeUnit.SECONDS)
        .writeTimeout(120, TimeUnit.SECONDS)
        .callTimeout(120, TimeUnit.SECONDS)
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(@ApplicationContext context: Context): Retrofit {
        val preferencesManager = PreferencesManager(context)

        val protocol = preferencesManager.getStringData("protocol", "http")
        val ipAddress = preferencesManager.getStringData("ipAddress", "10.0.2.2")
        val port = preferencesManager.getStringData("port", "5039")

        val baseUrl = "$protocol://$ipAddress:$port"

        val gson = GsonBuilder()
            .registerTypeAdapter(LocalDate::class.java, LDtoDT())
            .registerTypeAdapter(LocalDateTime::class.java, LDTtoDT())
            .registerTypeAdapter(Duration::class.java, DtoTS())
            .create()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun providePlaylistService(retrofit: Retrofit): PlaylistService {
        return retrofit.create(PlaylistService::class.java)
    }

    @Provides
    @Singleton
    fun provideSearchService(retrofit: Retrofit): SearchService {
        return retrofit.create(SearchService::class.java)
    }

    @Provides
    @Singleton
    fun provideSongService(retrofit: Retrofit): SongService {
        return retrofit.create(SongService::class.java)
    }

    @Provides
    @Singleton
    fun provideUserService(retrofit: Retrofit): UserService {
        return retrofit.create(UserService::class.java)
    }
}