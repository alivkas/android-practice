package com.example.android_practice.presentation.ui.screens.movielist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_practice.domain.model.Movie
import com.example.android_practice.domain.usecase.GetPopularMoviesUseCase
import com.example.android_practice.domain.usecase.SearchMoviesUseCase
import com.example.android_practice.presentation.state.UiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MovieListViewModel(
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase,
    private val searchMoviesUseCase: SearchMoviesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Movie>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<Movie>>> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private var searchJob: Job? = null
    private var isLoading = false

    init {
        loadPopularMovies()
    }

    fun loadPopularMovies() {
        if (isLoading) return

        isLoading = true
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val movies = getPopularMoviesUseCase()
                _uiState.value = if (movies.isEmpty()) {
                    UiState.Error("Фильмы не найдены")
                } else {
                    UiState.Success(movies)
                }
            } catch (e: Exception) {
                println("ViewModel ошибка: ${e.message}")
                _uiState.value = UiState.Error(e.message ?: "Неизвестная ошибка")
            } finally {
                isLoading = false
            }
        }
    }

    fun searchMovies(query: String) {
        _searchQuery.value = query

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500)

            if (query.isBlank()) {
                loadPopularMovies()
                return@launch
            }

            if (isLoading) return@launch

            isLoading = true
            _uiState.value = UiState.Loading
            try {
                val movies = searchMoviesUseCase(query)
                _uiState.value = if (movies.isEmpty()) {
                    UiState.Error("По запросу \"$query\" ничего не найдено")
                } else {
                    UiState.Success(movies)
                }
            } catch (e: Exception) {
                println("Ошибка поиска в ViewModel: ${e.message}")
                _uiState.value = UiState.Error(e.message ?: "Ошибка поиска")
            } finally {
                isLoading = false
            }
        }
    }

    fun clearSearch() {
        _searchQuery.value = ""
        loadPopularMovies()
    }

    fun retry() {
        if (_searchQuery.value.isBlank()) {
            loadPopularMovies()
        } else {
            searchMovies(_searchQuery.value)
        }
    }
}