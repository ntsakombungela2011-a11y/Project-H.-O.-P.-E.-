package com.example.darkhumor.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.darkhumor.domain.model.Joke

@Entity(tableName = "favorites")
data class FavoriteJokeEntity(
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
            flags = com.example.darkhumor.domain.model.JokeFlags(false, false, false, false, false, false), // Simplified for favorites
            safe = true,
            lang = "en"
        )
    }

    companion object {
        fun fromJoke(joke: Joke): FavoriteJokeEntity {
            return FavoriteJokeEntity(
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
