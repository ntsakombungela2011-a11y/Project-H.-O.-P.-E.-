package com.example.darkhumor.ui.theme

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.themeDataStore: DataStore<Preferences> by preferencesDataStore(name = "theme_preferences")

@Singleton
class ThemeManager @Inject constructor(@ApplicationContext context: Context) {
    
    private val dataStore = context.themeDataStore
    
    private object PreferencesKeys {
        val THEME = stringPreferencesKey("selected_theme")
    }
    
    val currentTheme: Flow<AppTheme> = dataStore.data.map { preferences ->
        val themeName = preferences[PreferencesKeys.THEME] ?: AppTheme.PURPLE.name
        try {
            AppTheme.valueOf(themeName)
        } catch (e: IllegalArgumentException) {
            AppTheme.PURPLE
        }
    }
    
    suspend fun setTheme(theme: AppTheme) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.THEME] = theme.name
        }
    }
}
