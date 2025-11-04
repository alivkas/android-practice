package com.example.android_practice.domain.usecase

import com.example.android_practice.domain.model.Movie
import com.example.android_practice.domain.repository.FavoriteRepository

class AddToFavoritesUseCase (
    private val repository: FavoriteRepository
) {
    suspend operator fun invoke(movie: Movie) = repository.addToFavorites(movie)
}