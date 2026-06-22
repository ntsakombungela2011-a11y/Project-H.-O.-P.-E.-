package com.example.darkhumor.data.api

import retrofit2.http.GET
import retrofit2.http.Query

interface JokeApiService {
    @GET("joke/Dark")
    suspend fun getJoke(
        @Query("blacklistFlags") blacklistFlags: String? = null
    ): JokeResponse
}
