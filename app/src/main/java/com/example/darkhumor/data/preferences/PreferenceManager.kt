package com.example.darkhumor.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

data class UserPreferences(
    val nsfw: Boolean = false,
    val religious: Boolean = false,
    val political: Boolean = false,
    val racist: Boolean = false,
    val sexist: Boolean = false,
    val explicit: Boolean = false
) {
    fun toBlacklistString(): String? {
        val flags = mutableListOf<String>()
        if (nsfw) flags.add("nsfw")
        if (religious) flags.add("religious")
        if (political) flags.add("political")
        if (racist) flags.add("racist")
        if (sexist) flags.add("sexist")
        if (explicit) flags.add("explicit")
        return if (flags.isEmpty()) null else flags.joinToString(",")
    }
}

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

@Singleton
class PreferenceManager @Inject constructor(@ApplicationContext context: Context) {

    private val dataStore = context.dataStore

    private object PreferencesKeys {
        val NSFW = booleanPreferencesKey("nsfw")
        val RELIGIOUS = booleanPreferencesKey("religious")
        val POLITICAL = booleanPreferencesKey("political")
        val RACIST = booleanPreferencesKey("racist")
        val SEXIST = booleanPreferencesKey("sexist")
        val EXPLICIT = booleanPreferencesKey("explicit")
    }

    val userPreferencesFlow: Flow<UserPreferences> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            UserPreferences(
                nsfw = preferences[PreferencesKeys.NSFW] ?: false,
                religious = preferences[PreferencesKeys.RELIGIOUS] ?: false,
                political = preferences[PreferencesKeys.POLITICAL] ?: false,
                racist = preferences[PreferencesKeys.RACIST] ?: false,
                sexist = preferences[PreferencesKeys.SEXIST] ?: false,
                explicit = preferences[PreferencesKeys.EXPLICIT] ?: false
            )
        }

    suspend fun updateNsfw(value: Boolean) {
        dataStore.edit { preferences -> preferences[PreferencesKeys.NSFW] = value }
    }

    suspend fun updateReligious(value: Boolean) {
        dataStore.edit { preferences -> preferences[PreferencesKeys.RELIGIOUS] = value }
    }

    suspend fun updatePolitical(value: Boolean) {
        dataStore.edit { preferences -> preferences[PreferencesKeys.POLITICAL] = value }
    }

    suspend fun updateRacist(value: Boolean) {
        dataStore.edit { preferences -> preferences[PreferencesKeys.RACIST] = value }
    }

    suspend fun updateSexist(value: Boolean) {
        dataStore.edit { preferences -> preferences[PreferencesKeys.SEXIST] = value }
    }

    suspend fun updateExplicit(value: Boolean) {
        dataStore.edit { preferences -> preferences[PreferencesKeys.EXPLICIT] = value }
    }
}
