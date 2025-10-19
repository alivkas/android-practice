package com.example.android_practice.data.repository.impl

import com.example.android_practice.data.api.KinopoiskApi
import com.example.android_practice.data.mapper.MovieMapper
import com.example.android_practice.domain.model.Movie
import com.example.android_practice.domain.repository.MovieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException

class MovieRepositoryImpl(
    private val api: KinopoiskApi
) : MovieRepository {

    override suspend fun getPopularMovies(page: Int): List<Movie> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getPopularMovies(page = page)
                response.movies.map { MovieMapper.toDomain(it) }
            } catch (e: Exception) {
                throw handleApiError(e)
            } as List<Movie>
        }
    }

    override suspend fun searchMovies(query: String, page: Int): List<Movie> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.searchMovies(query = query, page = page)
                response.movies.map { MovieMapper.toDomain(it) }
            } catch (e: Exception) {
                throw handleApiError(e)
            } as List<Movie>
        }
    }

    override suspend fun getMovieById(id: Int): Movie? {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getMovieById(id)
                MovieMapper.toDomain(response)
            } catch (e: Exception) {
                throw handleApiError(e)
            }
        }
    }

    private fun handleApiError(exception: Exception): Exception {
        return when (exception) {
            is UnknownHostException -> {
                Exception("Нет подключения к интернету. Проверьте соединение и попробуйте снова.")
            }
            is SocketTimeoutException -> {
                Exception("Таймаут соединения. Проверьте интернет соединение.")
            }
            is SSLHandshakeException -> {
                Exception("Ошибка безопасного соединения. Проверьте интернет.")
            }
            is retrofit2.HttpException -> {
                when (exception.code()) {
                    401 -> Exception("Неверный API ключ")
                    404 -> Exception("Фильм не найден")
                    429 -> Exception("Превышен лимит запросов")
                    500 -> Exception("Ошибка сервера")
                    502 -> Exception("Проблемы с сервером")
                    503 -> Exception("Сервер временно недоступен")
                    else -> Exception("Ошибка сервера: ${exception.code()}")
                }
            }
            is java.io.IOException -> {
                if (exception.message?.contains("Unable to resolve host") == true) {
                    Exception("Нет подключения к интернету. Проверьте сеть.")
                } else {
                    Exception("Сетевая ошибка: ${exception.message}")
                }
            }
            else -> {
                Exception("Неизвестная ошибка: ${exception.message ?: "Попробуйте позже"}")
            }
        }
    }
}