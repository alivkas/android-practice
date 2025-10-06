package com.example.android_practice.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Details : Screen("details/{movieId}") {
        fun createRoute(movieId: Int) = "details/$movieId"
    }
    object Favorites : Screen("favorites")
    object Profile : Screen("profile")
}