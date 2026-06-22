package com.example.darkhumor.data.repository

import com.example.darkhumor.data.api.JokeApiService
import com.example.darkhumor.data.db.CachedJokeEntity
import com.example.darkhumor.data.db.FavoriteJokeEntity
import com.example.darkhumor.data.db.JokeDao
import com.example.darkhumor.data.preferences.PreferenceManager
import com.example.darkhumor.domain.model.Joke
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JokeRepository @Inject constructor(
    private val apiService: JokeApiService,
    private val jokeDao: JokeDao,
    private val preferenceManager: PreferenceManager
) {
    suspend fun getJoke(): Result<Joke> {
        return try {
            val blacklist = preferenceManager.userPreferencesFlow.first().toBlacklistString()
            val response = apiService.getJoke(blacklist)
            val joke = response.toJoke()
            if (joke != null) {
                jokeDao.insertCachedJoke(CachedJokeEntity.fromJoke(joke))
                jokeDao.trimCache()
                Result.success(joke)
            } else {
                Result.failure(Exception("Failed to parse joke"))
            }
        } catch (e: Exception) {
            val cachedJokes = jokeDao.getCachedJokes()
            if (cachedJokes.isNotEmpty()) {
                Result.success(cachedJokes.random().toJoke())
            } else {
                Result.failure(e)
            }
        }
    }

    fun getFavoriteJokes(): Flow<List<Joke>> {
        return jokeDao.getFavoriteJokes().map { entities ->
            entities.map { it.toJoke() }
        }
    }

    suspend fun toggleFavorite(joke: Joke) {
        if (jokeDao.isFavorite(joke.id)) {
            jokeDao.deleteFavorite(FavoriteJokeEntity.fromJoke(joke))
        } else {
            jokeDao.insertFavorite(FavoriteJokeEntity.fromJoke(joke))
        }
    }

    suspend fun isFavorite(jokeId: Int): Boolean {
        return jokeDao.isFavorite(jokeId)
    }

    suspend fun deleteFavorite(joke: Joke) {
        jokeDao.deleteFavorite(FavoriteJokeEntity.fromJoke(joke))
    }
}
