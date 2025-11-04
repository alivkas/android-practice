package com.example.android_practice.domain.usecase

import com.example.android_practice.domain.repository.FavoriteRepository

class IsFavoriteUseCase(
    private val repository: FavoriteRepository
) {
    suspend operator fun invoke(movieId: Int): Boolean = repository.isFavorite(movieId)
}