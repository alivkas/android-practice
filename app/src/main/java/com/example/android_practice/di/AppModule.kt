package com.example.android_practice.di

import com.example.android_practice.data.repository.MovieRepository
import com.example.android_practice.data.repository.impl.MovieRepositoryImpl
import com.example.android_practice.ui.screens.moviedetails.MovieDetailsViewModel
import com.example.android_practice.ui.screens.movielist.MovieListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val repositoryModule = module {
    single<MovieRepository> { MovieRepositoryImpl() }
}

val viewModelModule = module {
    viewModel { MovieListViewModel(get()) }
    viewModel { MovieDetailsViewModel(get()) }
}

val appModule = listOf(repositoryModule, viewModelModule)