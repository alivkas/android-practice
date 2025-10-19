package com.example.android_practice.ui.screens.moviedetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_practice.data.Movie
import com.example.android_practice.data.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MovieDetailsViewModel(
    private val repository: MovieRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<MovieDetailsUiState>(MovieDetailsUiState.Loading)
    val uiState: StateFlow<MovieDetailsUiState> = _uiState.asStateFlow()

    fun loadMovie(movieId: Int) {
        viewModelScope.launch {
            try {
                val movie = repository.getMovieById(movieId)
                if (movie != null) {
                    _uiState.value = MovieDetailsUiState.Success(movie)
                } else {
                    _uiState.value = MovieDetailsUiState.Error("Movie not found")
                }
            } catch (e: Exception) {
                _uiState.value = MovieDetailsUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}

sealed class MovieDetailsUiState {
    object Loading : MovieDetailsUiState()
    data class Success(val movie: Movie) : MovieDetailsUiState()
    data class Error(val message: String) : MovieDetailsUiState()
}