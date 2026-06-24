package com.example.darkhumor.data.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface JokeDao {
    @Query("SELECT * FROM favorites ORDER BY timestamp DESC")
    fun getFavoriteJokes(): Flow<List<FavoriteJokeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(joke: FavoriteJokeEntity)

    @Delete
    suspend fun deleteFavorite(joke: FavoriteJokeEntity)

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE id = :id)")
    suspend fun isFavorite(id: Int): Boolean

    @Query("SELECT * FROM joke_cache ORDER BY timestamp DESC LIMIT 20")
    suspend fun getCachedJokes(): List<CachedJokeEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCachedJoke(joke: CachedJokeEntity)

    @Query("DELETE FROM joke_cache WHERE id NOT IN (SELECT id FROM joke_cache ORDER BY timestamp DESC LIMIT 20)")
    suspend fun trimCache()
}
