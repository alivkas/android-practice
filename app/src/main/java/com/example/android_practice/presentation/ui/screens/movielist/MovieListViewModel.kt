package com.example.android_practice.presentation.ui.screens.movielist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_practice.domain.model.FilterSettings
import com.example.android_practice.domain.model.Movie
import com.example.android_practice.domain.usecase.*
import com.example.android_practice.presentation.state.UiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MovieListViewModel(
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase,
    private val searchMoviesUseCase: SearchMoviesUseCase,
    private val getFilterSettingsUseCase: GetFilterSettingsUseCase,
    private val saveFilterSettingsUseCase: SaveFilterSettingsUseCase,
    private val addToFavoritesUseCase: AddToFavoritesUseCase,
    private val removeFromFavoritesUseCase: RemoveFromFavoritesUseCase,
    private val isFavoriteUseCase: IsFavoriteUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Movie>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<Movie>>> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _filterSettings = MutableStateFlow(FilterSettings())
    val filterSettings: StateFlow<FilterSettings> = _filterSettings.asStateFlow()

    private var allMovies: List<Movie> = emptyList()
    private var searchJob: Job? = null
    private var isLoading = false

    init {
        loadFilterSettings()
    }

    private fun loadFilterSettings() {
        viewModelScope.launch {
            getFilterSettingsUseCase().collect { settings ->
                _filterSettings.value = settings
                updateBadgeState(settings)
                if (allMovies.isNotEmpty()) {
                    applyFilters()
                }
            }
        }
    }

    private fun updateBadgeState(settings: FilterSettings) {
    }

    fun loadPopularMovies() {
        if (isLoading) return

        isLoading = true
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val movies = getPopularMoviesUseCase()
                allMovies = movies
                applyFilters()
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Неизвестная ошибка")
            } finally {
                isLoading = false
            }
        }
    }

    private fun applyFilters() {
        val filteredMovies = filterMovies(allMovies, _filterSettings.value)
        _uiState.value = if (filteredMovies.isEmpty()) {
            UiState.Error("Фильмы не найдены по выбранным фильтрам")
        } else {
            UiState.Success(filteredMovies)
        }
    }

    private fun filterMovies(movies: List<Movie>, filters: FilterSettings): List<Movie> {
        return movies.filter { movie ->
            (filters.genre.isEmpty() || movie.genre.contains(filters.genre, true)) &&
                    movie.rating >= filters.minRating &&
                    (filters.yearFrom == 0 || movie.year >= filters.yearFrom)
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
                allMovies = movies
                applyFilters()
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Ошибка поиска")
            } finally {
                isLoading = false
            }
        }
    }

    fun applyNewFilters(settings: FilterSettings) {
        viewModelScope.launch {
            saveFilterSettingsUseCase(settings)
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

    suspend fun addToFavorites(movie: Movie) {
        try {
            addToFavoritesUseCase(movie)
        } catch (e: Exception) {
            println("Ошибка добавления в избранное: ${e.message}")
        }
    }

    suspend fun removeFromFavorites(movieId: Int) {
        try {
            removeFromFavoritesUseCase(movieId)
        } catch (e: Exception) {
            println("Ошибка удаления из избранного: ${e.message}")
        }
    }

    suspend fun isFavorite(movieId: Int): Boolean {
        return try {
            isFavoriteUseCase(movieId)
        } catch (e: Exception) {
            println("Ошибка проверки избранного: ${e.message}")
            false
        }
    }

    fun isFavoriteFlow(movieId: Int): Flow<Boolean> {
        return flow {
            while (true) {
                emit(isFavoriteUseCase(movieId))
                delay(1000)
            }
        }
    }
}