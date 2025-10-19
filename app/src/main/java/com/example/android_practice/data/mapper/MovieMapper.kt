package com.example.android_practice.data.mapper

import com.example.android_practice.data.model.MovieDto
import com.example.android_practice.domain.model.Movie

object MovieMapper {

    fun toDomain(dto: MovieDto): Movie? {
        return Movie.createSafe(
            id = dto.id,
            title = dto.name,
            year = dto.year,
            genre = dto.genres?.joinToString(", ") { it.name ?: "" }
                ?.takeIf { it.isNotBlank() },
            posterUrl = dto.poster?.url,
            rating = dto.rating?.kp,
            description = dto.description?.takeIf { it.isNotBlank() },
            duration = dto.movieLength,
            actors = dto.persons
                ?.filter { it.profession == "actor" }
                ?.take(3)
                ?.mapNotNull { it.name }
                ?.filter { it.isNotBlank() },
            director = dto.persons
                ?.firstOrNull { it.profession == "director" }
                ?.name
                ?.takeIf { it.isNotBlank() },
            country = dto.countries?.joinToString(", ") { it.name ?: "" }
                ?.takeIf { it.isNotBlank() }
        )
    }
}