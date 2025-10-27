package com.example.android_practice.data.database.dao

import androidx.room.*
import com.example.android_practice.data.database.entity.FavoriteMovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteMovieDao {

    @Query("SELECT * FROM favorite_movies ORDER BY addedAt DESC")
    fun getAllFavorites(): Flow<List<FavoriteMovieEntity>>

    @Query("SELECT * FROM favorite_movies WHERE id = :movieId")
    suspend fun getFavoriteById(movieId: Int): FavoriteMovieEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToFavorites(movie: FavoriteMovieEntity)

    @Delete
    suspend fun removeFromFavorites(movie: FavoriteMovieEntity)

    @Query("DELETE FROM favorite_movies WHERE id = :movieId")
    suspend fun removeFromFavoritesById(movieId: Int)

    @Query("SELECT COUNT(*) FROM favorite_movies WHERE id = :movieId")
    suspend fun isFavorite(movieId: Int): Boolean
}