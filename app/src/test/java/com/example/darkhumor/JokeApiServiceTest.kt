package com.example.darkhumor

import com.example.darkhumor.data.api.JokeApiService
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer

class JokeApiServiceTest {

    private lateinit var server: MockWebServer
    private lateinit var apiService: JokeApiService

    @Before
    fun setup() {
        server = MockWebServer()
        val json = Json { ignoreUnknownKeys = true }
        val contentType = "application/json".toMediaType()
        apiService = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
            .create(JokeApiService::class.java)
    }

    @Test
    fun `test getJoke returns single joke correctly`() = runBlocking {
        val mockResponse = MockResponse()
            .setBody("""
                {
                    "error": false,
                    "category": "Dark",
                    "type": "single",
                    "joke": "Test Joke",
                    "flags": {
                        "nsfw": false,
                        "religious": false,
                        "political": false,
                        "racist": false,
                        "sexist": false,
                        "explicit": false
                    },
                    "id": 1,
                    "safe": true,
                    "lang": "en"
                }
            """.trimIndent())
        server.enqueue(mockResponse)

        val response = apiService.getJoke("Dark")
        assertEquals(false, response.error)
        assertEquals("single", response.type)
        assertEquals("Test Joke", response.joke)
    }

    @Test
    fun `test getJoke returns twopart joke correctly`() = runBlocking {
        val mockResponse = MockResponse()
            .setBody("""
                {
                    "error": false,
                    "category": "Dark",
                    "type": "twopart",
                    "setup": "Setup",
                    "delivery": "Delivery",
                    "flags": {
                        "nsfw": false,
                        "religious": false,
                        "political": false,
                        "racist": false,
                        "sexist": false,
                        "explicit": false
                    },
                    "id": 2,
                    "safe": true,
                    "lang": "en"
                }
            """.trimIndent())
        server.enqueue(mockResponse)

        val response = apiService.getJoke("Dark")
        assertEquals("twopart", response.type)
        assertEquals("Setup", response.setup)
        assertEquals("Delivery", response.delivery)
    }
}
