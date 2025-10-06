package com.example.android_practice.data.repository

import com.example.android_practice.data.Movie

interface MovieRepository {
    fun getAllMovies(): List<Movie>
    fun getMovieById(id: Int): Movie?
    fun searchMovies(query: String): List<Movie>
}