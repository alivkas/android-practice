package com.example.android_practice.domain.usecase

import com.example.android_practice.domain.model.Movie
import com.example.android_practice.domain.repository.MovieRepository

class GetMovieByIdUseCase(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(movieId: Int): Movie? {
        return repository.getMovieById(movieId)
    }
}