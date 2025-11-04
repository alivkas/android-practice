package com.example.android_practice.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home : BottomNavItem(
        route = "home",
        title = "Фильмы",
        icon = Icons.Default.Home
    )

    object Favorites : BottomNavItem(
        route = "favorites",
        title = "Избранное",
        icon = Icons.Default.Favorite
    )

    object Filters : BottomNavItem(
        route = "filters",
        title = "Фильтры",
        icon = Icons.Default.Settings
    )
    object Profile : BottomNavItem(
        route = "profile",
        title = "Профиль",
        icon = Icons.Default.Person
    )
}