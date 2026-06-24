package com.example.darkhumor.data.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface JokeApiService {
    @GET("joke/{category}")
    suspend fun getJoke(
        @Path("category") category: String,
        @Query("blacklistFlags") blacklistFlags: String? = null
    ): JokeResponse
}
