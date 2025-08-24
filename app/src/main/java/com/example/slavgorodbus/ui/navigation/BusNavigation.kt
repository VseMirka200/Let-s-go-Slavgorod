package com.example.slavgorodbus.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Home : Screen("home", "Маршруты", Icons.Filled.Home)
    object FavoriteTimes : Screen("favorite-times", "Избранное", Icons.Filled.AccessTime)
    object Settings : Screen("settings", "Настройки", Icons.Filled.Settings)
    object About : Screen("about", "О программе", Icons.Filled.Info)
}

val bottomNavItems = listOf(
    Screen.Home,
    Screen.FavoriteTimes,
    Screen.Settings
)

val routesThatShowBottomBar = listOf(
    Screen.Home.route,
    Screen.FavoriteTimes.route,
    Screen.Settings.route,
    Screen.About.route
)

@Composable
fun BottomNavigation(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = routesThatShowBottomBar.contains(currentRoute)

    if (showBottomBar) {
        NavigationBar {
            bottomNavItems.forEach { navigationItem ->
                val selected = when (currentRoute) {
                    Screen.About.route -> navigationItem.route == Screen.Settings.route
                    else -> currentRoute == navigationItem.route
                }

                NavigationBarItem(
                    icon = { Icon(navigationItem.icon, contentDescription = navigationItem.title) },
                    label = {
                        Text(
                            text = navigationItem.title,
                            softWrap = true,
                            maxLines = 1,
                            textAlign = TextAlign.Center
                        )
                    },
                    selected = selected,
                    onClick = {
                        val isCurrentlyOnAboutAndSettingsClicked = navigationItem.route == Screen.Settings.route && currentRoute == Screen.About.route

                        if (isCurrentlyOnAboutAndSettingsClicked) {
                        } else if (currentRoute != navigationItem.route) {
                            if (navigationItem.route == Screen.Settings.route) {
                                navController.navigate(Screen.Settings.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = false
                                }
                            } else {
                                navController.navigate(navigationItem.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                    }
                )
            }
        }
    }
}