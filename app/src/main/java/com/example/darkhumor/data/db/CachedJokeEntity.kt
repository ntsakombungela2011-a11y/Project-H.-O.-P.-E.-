package com.example.darkhumor.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.darkhumor.domain.model.Joke

@Entity(tableName = "joke_cache")
data class CachedJokeEntity(
    @PrimaryKey val id: Int,
    val category: String,
    val type: String,
    val joke: String?,
    val setup: String?,
    val delivery: String?,
    val timestamp: Long = System.currentTimeMillis()
) {
    fun toJoke(): Joke {
        return Joke(
            id = id,
            category = category,
            type = type,
            joke = joke,
            setup = setup,
            delivery = delivery,
            flags = com.example.darkhumor.domain.model.JokeFlags(false, false, false, false, false, false),
            safe = true,
            lang = "en"
        )
    }

    companion object {
        fun fromJoke(joke: Joke): CachedJokeEntity {
            return CachedJokeEntity(
                id = joke.id,
                category = joke.category,
                type = joke.type,
                joke = joke.joke,
                setup = joke.setup,
                delivery = joke.delivery
            )
        }
    }
}
