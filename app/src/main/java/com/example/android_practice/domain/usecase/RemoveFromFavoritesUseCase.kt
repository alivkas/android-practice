package com.example.android_practice.domain.usecase

import com.example.android_practice.domain.repository.FavoriteRepository

class RemoveFromFavoritesUseCase(
    private val repository: FavoriteRepository
) {
    suspend operator fun invoke(movieId: Int) = repository.removeFromFavorites(movieId)
}