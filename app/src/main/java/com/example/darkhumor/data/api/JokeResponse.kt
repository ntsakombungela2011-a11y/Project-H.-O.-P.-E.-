package com.example.darkhumor.data.api

import com.example.darkhumor.domain.model.Joke
import kotlinx.serialization.Serializable

@Serializable
data class JokeResponse(
    val error: Boolean,
    val amount: Int? = null,
    val jokes: List<Joke>? = null,
    // Single joke response fields
    val id: Int? = null,
    val category: String? = null,
    val type: String? = null,
    val joke: String? = null,
    val setup: String? = null,
    val delivery: String? = null,
    val flags: com.example.darkhumor.domain.model.JokeFlags? = null,
    val safe: Boolean? = null,
    val lang: String? = null
) {
    fun toJoke(): Joke? {
        if (id == null || category == null || type == null) return null
        return Joke(
            id = id,
            category = category,
            type = type,
            joke = joke,
            setup = setup,
            delivery = delivery,
            flags = flags ?: com.example.darkhumor.domain.model.JokeFlags(false, false, false, false, false, false),
            safe = safe ?: true,
            lang = lang ?: "en"
        )
    }
}
