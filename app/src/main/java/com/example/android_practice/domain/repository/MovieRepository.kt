package com.example.android_practice.domain.repository

import com.example.android_practice.domain.model.Movie

interface MovieRepository {
    suspend fun getPopularMovies(page: Int): List<Movie>
    suspend fun searchMovies(query: String, page: Int): List<Movie>
    suspend fun getMovieById(id: Int): Movie?
}