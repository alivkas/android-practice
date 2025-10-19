package com.example.android_practice.ui.screens.movielist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_practice.data.Movie
import com.example.android_practice.data.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MovieListViewModel(
    private val repository: MovieRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<MovieListUiState>(MovieListUiState.Loading)
    val uiState: StateFlow<MovieListUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    init {
        loadMovies()
    }

    fun loadMovies() {
        viewModelScope.launch {
            try {
                val movies = repository.getAllMovies()
                _uiState.value = MovieListUiState.Success(movies)
            } catch (e: Exception) {
                _uiState.value = MovieListUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun searchMovies(query: String) {
        _searchQuery.value = query
        viewModelScope.launch {
            if (query.isBlank()) {
                loadMovies()
            } else {
                val movies = repository.searchMovies(query)
                _uiState.value = MovieListUiState.Success(movies)
            }
        }
    }
}

sealed class MovieListUiState {
    object Loading : MovieListUiState()
    data class Success(val movies: List<Movie>) : MovieListUiState()
    data class Error(val message: String) : MovieListUiState()
}