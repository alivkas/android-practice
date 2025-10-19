package com.example.android_practice.di

import com.example.android_practice.data.api.ApiClient
import com.example.android_practice.data.api.KinopoiskApi
import com.example.android_practice.data.repository.impl.MovieRepositoryImpl
import com.example.android_practice.domain.repository.MovieRepository
import com.example.android_practice.domain.usecase.GetMovieByIdUseCase
import com.example.android_practice.domain.usecase.GetPopularMoviesUseCase
import com.example.android_practice.domain.usecase.SearchMoviesUseCase
import com.example.android_practice.presentation.ui.screens.moviedetails.MovieDetailsViewModel
import com.example.android_practice.presentation.ui.screens.movielist.MovieListViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val networkModule = module {
    single<KinopoiskApi> {
        ApiClient.create(androidContext())
    }
}

val dataModule = module {
    single<MovieRepository> {
        MovieRepositoryImpl(get())
    }
}

val domainModule = module {
    factory { GetPopularMoviesUseCase(get()) }
    factory { SearchMoviesUseCase(get()) }
    factory { GetMovieByIdUseCase(get()) }
}

val viewModelModule = module {
    viewModel { MovieListViewModel(get(), get()) }
    viewModel { MovieDetailsViewModel(get()) }
}

val appModule = listOf(networkModule, dataModule, domainModule, viewModelModule)