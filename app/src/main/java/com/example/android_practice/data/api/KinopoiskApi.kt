package com.example.android_practice.data.api

import com.example.android_practice.data.model.MovieDto
import com.example.android_practice.data.model.MoviesResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface KinopoiskApi {

    @GET("v1.4/movie")
    suspend fun getPopularMovies(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 100,
        @Query("selectFields") selectFields: List<String> = listOf(
            "id", "name", "year", "genres", "poster", "rating",
            "description", "movieLength", "persons", "countries"
        ),
        @Query("sortField") sortField: String = "rating.kp",
        @Query("sortType") sortType: String = "-1",
        @Query("rating.kp") rating: String = "7-10"
    ): MoviesResponseDto

    @GET("v1.4/movie/search")
    suspend fun searchMovies(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 100,
        @Query("query") query: String,
        @Query("selectFields") selectFields: List<String> = listOf(
            "id", "name", "year", "genres", "poster", "rating",
            "description", "movieLength", "persons", "countries"
        )
    ): MoviesResponseDto

    @GET("v1.4/movie/{id}")
    suspend fun getMovieById(
        @Path("id") id: Int,
        @Query("selectFields") selectFields: List<String> = listOf(
            "id", "name", "year", "genres", "poster", "rating",
            "description", "movieLength", "persons", "countries"
        )
    ): MovieDto
}