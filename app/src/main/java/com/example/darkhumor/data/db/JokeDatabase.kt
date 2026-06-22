package com.example.darkhumor.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [FavoriteJokeEntity::class, CachedJokeEntity::class], version = 1, exportSchema = false)
abstract class JokeDatabase : RoomDatabase() {
    abstract fun jokeDao(): JokeDao
}
