package com.example.android_practice.presentation.ui.screens.moviedetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_practice.domain.model.Movie
import com.example.android_practice.domain.usecase.AddToFavoritesUseCase
import com.example.android_practice.domain.usecase.GetMovieByIdUseCase
import com.example.android_practice.domain.usecase.IsFavoriteUseCase
import com.example.android_practice.domain.usecase.RemoveFromFavoritesUseCase
import com.example.android_practice.presentation.state.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MovieDetailsViewModel(
    private val getMovieByIdUseCase: GetMovieByIdUseCase,
    private val addToFavoritesUseCase: AddToFavoritesUseCase,
    private val removeFromFavoritesUseCase: RemoveFromFavoritesUseCase,
    private val isFavoriteUseCase: IsFavoriteUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<Movie>>(UiState.Loading)
    val uiState: StateFlow<UiState<Movie>> = _uiState.asStateFlow()

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite.asStateFlow()

    fun loadMovie(movieId: Int) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val movie = getMovieByIdUseCase(movieId)
                if (movie != null) {
                    _uiState.value = UiState.Success(movie)
                    _isFavorite.value = isFavoriteUseCase(movieId)
                } else {
                    _uiState.value = UiState.Error("Фильм не найден")
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Ошибка загрузки")
            }
        }
    }

    fun toggleFavorite(movie: Movie) {
        viewModelScope.launch {
            try {
                if (_isFavorite.value) {
                    removeFromFavoritesUseCase(movie.id)
                    _isFavorite.value = false
                } else {
                    addToFavoritesUseCase(movie)
                    _isFavorite.value = true
                }
            } catch (e: Exception) {
                println("Ошибка избранного: ${e.message}")
            }
        }
    }
}