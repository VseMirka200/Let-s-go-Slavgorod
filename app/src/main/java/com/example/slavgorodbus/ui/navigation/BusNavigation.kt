package com.example.slavgorodbus.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.Icon
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
    object FavoriteTimes : Screen("favorite-times", "Избранное", Icons.Filled.AccessTime) // Можно уточнить "Избранное время" если хотите
    object About : Screen("about", "О программе", Icons.AutoMirrored.Filled.HelpOutline)
}

// Список элементов для нижней навигации
val bottomNavItems = listOf(
    Screen.Home,
    Screen.Search,
    Screen.FavoriteTimes,
    Screen.About
)

@Composable
fun BottomNavigation(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Отображаем BottomNavigation только если текущий маршрут есть в bottomNavItems.
    // Так как "О программе" теперь в bottomNavItems, панель будет видна и на этом экране.
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
                            softWrap = true, // Позволяет тексту переноситься, если он не помещается
                            maxLines = 1,    // Ограничиваем одной строкой для компактности в BottomBar (или 2, если предпочитаете)
                            textAlign = TextAlign.Center
                        )
                    },
                    selected = selected,
                    onClick = {
                        if (!selected) {
                            navController.navigate(navigationItem.route) {
                                // Эта логика popUpTo/launchSingleTop/restoreState хороша для
                                // предотвращения накопления стека при переключении вкладок
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
