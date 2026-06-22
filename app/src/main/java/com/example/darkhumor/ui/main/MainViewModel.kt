package com.example.darkhumor.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.darkhumor.data.repository.JokeRepository
import com.example.darkhumor.domain.model.Joke
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: JokeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<MainUiState>(MainUiState.Empty)
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite.asStateFlow()

    fun getJoke() {
        viewModelScope.launch {
            _uiState.value = MainUiState.Loading
            repository.getJoke()
                .onSuccess { joke ->
                    _uiState.value = MainUiState.Success(joke)
                    _isFavorite.value = repository.isFavorite(joke.id)
                }
                .onFailure { error ->
                    _uiState.value = MainUiState.Error(error.message ?: "Unknown error")
                }
        }
    }

    fun toggleFavorite(joke: Joke) {
        viewModelScope.launch {
            repository.toggleFavorite(joke)
            _isFavorite.value = repository.isFavorite(joke.id)
        }
    }
}

sealed class MainUiState {
    object Empty : MainUiState()
    object Loading : MainUiState()
    data class Success(val joke: Joke) : MainUiState()
    data class Error(val message: String) : MainUiState()
}
