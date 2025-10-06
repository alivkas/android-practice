package com.example.android_practice.data.repository.impl

import com.example.android_practice.data.Movie
import com.example.android_practice.data.mock.MockData
import com.example.android_practice.data.repository.MovieRepository

class MovieRepositoryImpl : MovieRepository {
    override fun getAllMovies(): List<Movie> {
        return MockData.movies
    }

    override fun getMovieById(id: Int): Movie? {
        return MockData.movies.find { it.id == id }
    }

    override fun searchMovies(query: String): List<Movie> {
        return MockData.movies.filter {
            it.title.contains(query, ignoreCase = true) ||
                    it.genre.contains(query, ignoreCase = true)
        }
    }
}