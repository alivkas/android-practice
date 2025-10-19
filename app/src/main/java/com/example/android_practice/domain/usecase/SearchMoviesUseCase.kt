package com.example.android_practice.domain.usecase

import com.example.android_practice.domain.model.Movie
import com.example.android_practice.domain.repository.MovieRepository

class SearchMoviesUseCase(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(query: String, page: Int = 1): List<Movie> {
        if (query.isBlank()) return emptyList()
        return repository.searchMovies(query, page)
    }
}