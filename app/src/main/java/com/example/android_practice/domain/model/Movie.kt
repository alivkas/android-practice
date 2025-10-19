package com.example.android_practice.domain.model

data class Movie(
    val id: Int,
    val title: String,
    val year: Int,
    val genre: String,
    val posterUrl: String,
    val rating: Double,
    val description: String,
    val duration: Int,
    val actors: List<String>,
    val director: String,
    val country: String
) {
    companion object {
        fun createSafe(
            id: Int?,
            title: String?,
            year: Int?,
            genre: String?,
            posterUrl: String?,
            rating: Double?,
            description: String?,
            duration: Int?,
            actors: List<String>?,
            director: String?,
            country: String?
        ): Movie? {
            val safeId = id ?: return null
            val safeTitle = title ?: "Без названия"
            val safeYear = year ?: 2023

            return Movie(
                id = safeId,
                title = safeTitle,
                year = safeYear,
                genre = genre ?: "Неизвестно",
                posterUrl = posterUrl ?: "",
                rating = rating ?: 0.0,
                description = description ?: "Описание отсутствует",
                duration = duration ?: 0,
                actors = actors ?: emptyList(),
                director = director ?: "Неизвестно",
                country = country ?: "Неизвестно"
            )
        }
    }
}