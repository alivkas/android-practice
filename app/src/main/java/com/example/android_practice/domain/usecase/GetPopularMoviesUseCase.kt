package com.example.android_practice.domain.usecase

import com.example.android_practice.domain.model.Movie
import com.example.android_practice.domain.repository.MovieRepository

class GetPopularMoviesUseCase(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(page: Int = 1): List<Movie> {
        return repository.getPopularMovies(page)
    }
}