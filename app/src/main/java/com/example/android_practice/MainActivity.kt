package com.example.android_practice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.android_practice.data.cache.BadgeStateManager
import com.example.android_practice.di.appModule
import com.example.android_practice.navigation.BottomNavItem
import com.example.android_practice.navigation.mainNavGraph
import com.example.android_practice.presentation.ui.theme.AndroidpracticeTheme
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startKoin {
            androidContext(application)
            modules(appModule)
        }

        setContent {
            AndroidpracticeTheme {
                MainApp()
            }
        }
    }
}

@Composable
fun MainApp() {
    val navController = rememberNavController()
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStack?.destination
    var showBadge by remember { mutableStateOf(BadgeStateManager.shouldShowBadge()) }

    androidx.compose.runtime.LaunchedEffect(currentDestination) {
        showBadge = BadgeStateManager.shouldShowBadge()
    }

    Scaffold(
        bottomBar = {
            NavigationBar {
                listOf(
                    BottomNavItem.Home,
                    BottomNavItem.Favorites,
                    BottomNavItem.Filters,
                    BottomNavItem.Profile
                ).forEach { item ->
                    NavigationBarItem(
                        icon = {
                            if (item.route == "filters") {
                                BadgedBox(
                                    badge = {
                                        if (showBadge) {
                                            Badge()
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = item.icon,
                                        contentDescription = item.title
                                    )
                                }
                            } else {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.title
                                )
                            }
                        },
                        label = { Text(item.title) },
                        selected = currentDestination?.route == item.route,
                        onClick = {
                            navController.navigate(item.route) {
                                launchSingleTop = true
                                restoreState = true
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                            }
                            showBadge = BadgeStateManager.shouldShowBadge()
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "main",
            modifier = Modifier.padding(paddingValues)
        ) {
            mainNavGraph(navController)
        }
    }
}