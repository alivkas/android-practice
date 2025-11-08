package com.example.android_practice.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.room.Room
import com.example.android_practice.data.api.ApiClient
import com.example.android_practice.data.api.KinopoiskApi
import com.example.android_practice.data.cache.BadgeCache
import com.example.android_practice.data.database.MovieDatabase
import com.example.android_practice.data.database.dao.FavoriteMovieDao
import com.example.android_practice.data.datastore.FilterPreferences
import com.example.android_practice.data.datastore.filterDataStore
import com.example.android_practice.data.manager.DownloadManager
import com.example.android_practice.data.manager.ImageManager
import com.example.android_practice.data.repository.impl.FavoriteRepositoryImpl
import com.example.android_practice.data.repository.impl.FilterRepositoryImpl
import com.example.android_practice.data.repository.impl.MovieRepositoryImpl
import com.example.android_practice.data.repository.impl.ProfileRepositoryImpl
import com.example.android_practice.domain.repository.FavoriteRepository
import com.example.android_practice.domain.repository.FilterRepository
import com.example.android_practice.domain.repository.MovieRepository
import com.example.android_practice.domain.repository.ProfileRepository
import com.example.android_practice.domain.usecase.*
import com.example.android_practice.presentation.ui.screens.favorites.FavoritesViewModel
import com.example.android_practice.presentation.ui.screens.filters.FilterViewModel
import com.example.android_practice.presentation.ui.screens.moviedetails.MovieDetailsViewModel
import com.example.android_practice.presentation.ui.screens.movielist.MovieListViewModel
import com.example.android_practice.presentation.ui.screens.profile.EditProfileViewModel
import com.example.android_practice.presentation.ui.screens.profile.ProfileViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single<KinopoiskApi> {
        ApiClient.create(androidContext())
    }

    single<MovieDatabase> {
        Room.databaseBuilder(
            androidContext(),
            MovieDatabase::class.java,
            "movie_database"
        ).build()
    }

    single<FavoriteMovieDao> {
        get<MovieDatabase>().favoriteMovieDao()
    }

    single<DataStore<Preferences>> {
        androidContext().filterDataStore
    }

    single<FilterPreferences> {
        FilterPreferences(get())
    }

    single<BadgeCache> { BadgeCache() }

    single<MovieRepository> {
        MovieRepositoryImpl(get())
    }

    single<FavoriteRepository> {
        FavoriteRepositoryImpl(get())
    }

    single<FilterRepository> {
        FilterRepositoryImpl(get())
    }

    single<ProfileRepository> {
        ProfileRepositoryImpl(get())
    }

    factory<GetPopularMoviesUseCase> { GetPopularMoviesUseCase(get()) }
    factory<SearchMoviesUseCase> { SearchMoviesUseCase(get()) }
    factory<GetMovieByIdUseCase> { GetMovieByIdUseCase(get()) }
    factory<AddToFavoritesUseCase> { AddToFavoritesUseCase(get()) }
    factory<RemoveFromFavoritesUseCase> { RemoveFromFavoritesUseCase(get()) }
    factory<GetAllFavoritesUseCase> { GetAllFavoritesUseCase(get()) }
    factory<IsFavoriteUseCase> { IsFavoriteUseCase(get()) }
    factory<GetFilterSettingsUseCase> { GetFilterSettingsUseCase(get()) }
    factory<SaveFilterSettingsUseCase> { SaveFilterSettingsUseCase(get()) }
    factory<GetProfileUseCase> { GetProfileUseCase(get()) }
    factory<SaveProfileUseCase> { SaveProfileUseCase(get()) }
    factory { ImageManager(get()) }
    factory { DownloadManager(get()) }

    viewModel<MovieListViewModel> {
        MovieListViewModel(
            getPopularMoviesUseCase = get(),
            searchMoviesUseCase = get(),
            getFilterSettingsUseCase = get(),
            saveFilterSettingsUseCase = get(),
            addToFavoritesUseCase = get(),
            removeFromFavoritesUseCase = get(),
            isFavoriteUseCase = get()
        )
    }

    viewModel<MovieDetailsViewModel> {
        MovieDetailsViewModel(
            getMovieByIdUseCase = get(),
            addToFavoritesUseCase = get(),
            removeFromFavoritesUseCase = get(),
            isFavoriteUseCase = get()
        )
    }

    viewModel<FavoritesViewModel> {
        FavoritesViewModel(
            getAllFavoritesUseCase = get()
        )
    }

    viewModel<FilterViewModel> {
        FilterViewModel(
            getFilterSettingsUseCase = get(),
            saveFilterSettingsUseCase = get(),
        )
    }

    viewModel<ProfileViewModel> {
        ProfileViewModel(
            getProfileUseCase = get(),
            downloadManager = get()
        )
    }

    viewModel<EditProfileViewModel> {
        EditProfileViewModel(
            androidApplication(),
            get(),
            get(),
            get()
        )
    }
}