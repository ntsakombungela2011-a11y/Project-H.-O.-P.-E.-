package com.example.darkhumor.di

import android.content.Context
import androidx.room.Room
import com.example.darkhumor.data.api.JokeApiService
import com.example.darkhumor.data.db.JokeDao
import com.example.darkhumor.data.db.JokeDatabase
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    @Provides
    @Singleton
    fun provideRetrofit(json: Json): Retrofit {
        val contentType = MediaType.get("application/json")
        return Retrofit.Builder()
            .baseUrl("https://v2.jokeapi.dev/")
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }

    @Provides
    @Singleton
    fun provideJokeApiService(retrofit: Retrofit): JokeApiService {
        return retrofit.create(JokeApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideJokeDatabase(@ApplicationContext context: Context): JokeDatabase {
        return Room.databaseBuilder(
            context,
            JokeDatabase::class.java,
            "joke_database"
        ).build()
    }

    @Provides
    fun provideJokeDao(database: JokeDatabase): JokeDao {
        return database.jokeDao()
    }
}
