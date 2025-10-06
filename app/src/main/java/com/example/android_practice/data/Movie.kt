package com.example.android_practice.data

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
)
