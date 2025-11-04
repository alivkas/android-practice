package com.example.android_practice.data.repository.impl

import com.example.android_practice.data.database.dao.FavoriteMovieDao
import com.example.android_practice.data.mapper.FavoriteMovieMapper
import com.example.android_practice.domain.model.Movie
import com.example.android_practice.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoriteRepositoryImpl(
    private val favoriteMovieDao: FavoriteMovieDao
) : FavoriteRepository {

    override fun getAllFavorites(): Flow<List<Movie>> {
        return favoriteMovieDao.getAllFavorites().map { entities ->
            entities.mapNotNull { FavoriteMovieMapper.toDomain(it) }
        }
    }

    override suspend fun addToFavorites(movie: Movie) {
        val entity = FavoriteMovieMapper.toEntity(movie)
        favoriteMovieDao.addToFavorites(entity)
    }

    override suspend fun removeFromFavorites(movieId: Int) {
        favoriteMovieDao.removeFromFavoritesById(movieId)
    }

    override suspend fun isFavorite(movieId: Int): Boolean {
        return favoriteMovieDao.isFavorite(movieId)
    }
}