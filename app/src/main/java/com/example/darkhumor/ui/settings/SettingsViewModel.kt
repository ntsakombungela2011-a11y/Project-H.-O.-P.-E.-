package com.example.darkhumor.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.darkhumor.data.preferences.PreferenceManager
import com.example.darkhumor.data.preferences.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferenceManager: PreferenceManager
) : ViewModel() {

    val preferences: StateFlow<UserPreferences> = preferenceManager.userPreferencesFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UserPreferences())

    fun updateNsfw(value: Boolean) = viewModelScope.launch { preferenceManager.updateNsfw(value) }
    fun updateReligious(value: Boolean) = viewModelScope.launch { preferenceManager.updateReligious(value) }
    fun updatePolitical(value: Boolean) = viewModelScope.launch { preferenceManager.updatePolitical(value) }
    fun updateRacist(value: Boolean) = viewModelScope.launch { preferenceManager.updateRacist(value) }
    fun updateSextist(value: Boolean) = viewModelScope.launch { preferenceManager.updateSexist(value) }
    fun updateExplicit(value: Boolean) = viewModelScope.launch { preferenceManager.updateExplicit(value) }
}
