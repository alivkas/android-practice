package com.example.android_practice.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_movies")
data class FavoriteMovieEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val year: Int,
    val genre: String,
    val posterUrl: String,
    val rating: Double,
    val description: String,
    val duration: Int,
    val actors: String,
    val director: String,
    val country: String,
    val addedAt: Long = System.currentTimeMillis()
)