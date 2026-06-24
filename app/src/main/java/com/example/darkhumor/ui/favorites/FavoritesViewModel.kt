package com.example.darkhumor.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.darkhumor.data.repository.JokeRepository
import com.example.darkhumor.domain.model.Joke
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val repository: JokeRepository
) : ViewModel() {

    val favorites: StateFlow<List<Joke>> = repository.getFavoriteJokes()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun removeFavorite(joke: Joke) {
        viewModelScope.launch {
            repository.deleteFavorite(joke)
        }
    }
}
