package com.example.slavgorodbus.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Home : Screen("home", "Маршруты", Icons.Filled.Home)
    object Search : Screen("search", "Поиск", Icons.Filled.Search)
    object FavoriteTimes : Screen("favorite-times", "Избранное", Icons.Filled.AccessTime)
    object Settings : Screen("settings", "Настройки", Icons.Filled.Settings)
    object About : Screen("about", "О программе", Icons.Filled.Info)
}

val bottomNavItems = listOf(
    Screen.Home,
    Screen.Search,
    Screen.FavoriteTimes,
    Screen.Settings
)

@Composable
fun BottomNavigation(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = bottomNavItems.any { it.route == currentRoute }

    if (showBottomBar) {
        NavigationBar {
            bottomNavItems.forEach { navigationItem ->
                val selected = currentRoute == navigationItem.route
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
                        if (!selected) {
                            navController.navigate(navigationItem.route) {
                                navController.graph.startDestinationRoute?.let { startRoute ->
                                    popUpTo(startRoute) {
                                        saveState = true
                                    }
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                )
            }
        }
    }
}