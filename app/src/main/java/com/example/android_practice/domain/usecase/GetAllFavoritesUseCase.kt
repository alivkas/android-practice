package com.example.android_practice.domain.usecase

import com.example.android_practice.domain.model.Movie
import com.example.android_practice.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow

class GetAllFavoritesUseCase(
    private val repository: FavoriteRepository
) {
    operator fun invoke(): Flow<List<Movie>> = repository.getAllFavorites()
}