package com.example.android_practice.data.model

import com.google.gson.annotations.SerializedName

data class MoviesResponseDto(
    @SerializedName("docs") val movies: List<MovieDto>,
    @SerializedName("page") val page: Int,
    @SerializedName("pages") val totalPages: Int,
    @SerializedName("total") val total: Int
)

data class MovieDto(
    @SerializedName("id") val id: Int?,
    @SerializedName("name") val name: String?,
    @SerializedName("year") val year: Int?,
    @SerializedName("description") val description: String?,
    @SerializedName("movieLength") val movieLength: Int?,

    @SerializedName("poster") val poster: PosterDto?,
    @SerializedName("rating") val rating: RatingDto?,
    @SerializedName("genres") val genres: List<GenreDto>?,
    @SerializedName("countries") val countries: List<CountryDto>?,
    @SerializedName("persons") val persons: List<PersonDto>?
)

data class PosterDto(
    @SerializedName("url") val url: String?,
    @SerializedName("previewUrl") val previewUrl: String?
)

data class RatingDto(
    @SerializedName("kp") val kp: Double?,
    @SerializedName("imdb") val imdb: Double?,
    @SerializedName("filmCritics") val filmCritics: Double?,
    @SerializedName("russianFilmCritics") val russianFilmCritics: Double?
)

data class GenreDto(
    @SerializedName("name") val name: String?
)

data class CountryDto(
    @SerializedName("name") val name: String?
)

data class PersonDto(
    @SerializedName("id") val id: Int?,
    @SerializedName("name") val name: String?,
    @SerializedName("enName") val enName: String?,
    @SerializedName("photo") val photo: String?,
    @SerializedName("profession") val profession: String?,
    @SerializedName("enProfession") val enProfession: String?
)