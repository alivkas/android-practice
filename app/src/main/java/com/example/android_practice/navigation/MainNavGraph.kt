package com.example.android_practice.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.android_practice.presentation.ui.screens.favorites.FavoritesScreen
import com.example.android_practice.presentation.ui.screens.filters.FilterScreen
import com.example.android_practice.presentation.ui.screens.moviedetails.MovieDetailsScreen
import com.example.android_practice.presentation.ui.screens.movielist.MovieListScreen

fun NavGraphBuilder.mainNavGraph(navController: NavHostController) {
    navigation(
        startDestination = Screen.Home.route,
        route = "main"
    ) {
        composable(route = Screen.Home.route) {
            MovieListScreen(
                onMovieClick = { movieId ->
                    navController.navigate(Screen.Details.createRoute(movieId))
                },
                onFiltersClick = {
                    navController.navigate(Screen.Filters.route)
                }
            )
        }

        composable(
            route = Screen.Details.route,
            arguments = listOf(navArgument("movieId") { type = NavType.IntType })
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getInt("movieId") ?: 0
            MovieDetailsScreen(
                movieId = movieId,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(route = Screen.Favorites.route) {
            FavoritesScreen(
                onBackClick = { navController.popBackStack() },
                onMovieClick = { movieId ->
                    navController.navigate(Screen.Details.createRoute(movieId))
                }
            )
        }

        composable(route = Screen.Filters.route) {
            FilterScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onApplyFilters = { settings ->
                    navController.popBackStack()
                }
            )
        }
    }
}