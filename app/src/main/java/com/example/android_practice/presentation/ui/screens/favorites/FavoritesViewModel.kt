package com.example.android_practice.presentation.ui.screens.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_practice.domain.model.Movie
import com.example.android_practice.domain.usecase.GetAllFavoritesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val getAllFavoritesUseCase: GetAllFavoritesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(FavoritesUiState())
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()

    fun loadFavorites() {
        _uiState.value = _uiState.value.copy(isLoading = true)
        viewModelScope.launch {
            getAllFavoritesUseCase().collect { movies ->
                _uiState.value = FavoritesUiState(
                    isLoading = false,
                    movies = movies
                )
            }
        }
    }
}

data class FavoritesUiState(
    val isLoading: Boolean = true,
    val movies: List<Movie> = emptyList()
)