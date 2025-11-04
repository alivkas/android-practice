package com.example.android_practice.domain.model

data class FilterSettings(
    val genre: String = "",
    val minRating: Double = 0.0,
    val yearFrom: Int = 0
) {
    val isDefault: Boolean
        get() = genre.isEmpty() && minRating == 0.0 && yearFrom == 0
}