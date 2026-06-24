package com.example.darkhumor.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Joke(
    val id: Int,
    val category: String,
    val type: String, // "single" or "twopart"
    val joke: String? = null,
    val setup: String? = null,
    val delivery: String? = null,
    val flags: JokeFlags,
    val safe: Boolean,
    val lang: String
)

@Serializable
data class JokeFlags(
    val nsfw: Boolean,
    val religious: Boolean,
    val political: Boolean,
    val racist: Boolean,
    val sexist: Boolean,
    val explicit: Boolean
)
