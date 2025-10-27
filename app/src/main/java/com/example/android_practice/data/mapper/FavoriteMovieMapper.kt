package com.example.android_practice.data.mapper

import com.example.android_practice.data.database.entity.FavoriteMovieEntity
import com.example.android_practice.domain.model.Movie
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object FavoriteMovieMapper {

    private val json = Json { ignoreUnknownKeys = true }

    fun toEntity(movie: Movie): FavoriteMovieEntity {
        return FavoriteMovieEntity(
            id = movie.id,
            title = movie.title,
            year = movie.year,
            genre = movie.genre,
            posterUrl = movie.posterUrl,
            rating = movie.rating,
            description = movie.description,
            duration = movie.duration,
            actors = json.encodeToString(movie.actors),
            director = movie.director,
            country = movie.country
        )
    }

    fun toDomain(entity: FavoriteMovieEntity): Movie? {
        return try {
            Movie(
                id = entity.id,
                title = entity.title,
                year = entity.year,
                genre = entity.genre,
                posterUrl = entity.posterUrl,
                rating = entity.rating,
                description = entity.description,
                duration = entity.duration,
                actors = json.decodeFromString(entity.actors),
                director = entity.director,
                country = entity.country
            )
        } catch (e: Exception) {
            null
        }
    }
}