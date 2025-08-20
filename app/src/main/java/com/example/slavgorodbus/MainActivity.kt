package com.example.slavgorodbus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels // Для by viewModels()
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext // Для ThemeViewModel в BusScheduleApp, если SettingsScreen его там получает
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.slavgorodbus.ui.navigation.BottomNavigation
import com.example.slavgorodbus.ui.navigation.Screen
import com.example.slavgorodbus.ui.screens.AboutScreen
import com.example.slavgorodbus.ui.screens.FavoriteTimesScreen
import com.example.slavgorodbus.ui.screens.HomeScreen
import com.example.slavgorodbus.ui.screens.ScheduleScreen
import com.example.slavgorodbus.ui.screens.SearchScreen
import com.example.slavgorodbus.ui.screens.SettingsScreen
import com.example.slavgorodbus.ui.theme.SlavgorodBusTheme // Ваша тема
import com.example.slavgorodbus.ui.viewmodel.AppTheme
import com.example.slavgorodbus.ui.viewmodel.BusViewModel
import com.example.slavgorodbus.ui.viewmodel.ThemeViewModel
import com.example.slavgorodbus.ui.viewmodel.ThemeViewModelFactory
import com.example.slavgorodbus.ui.viewmodel.getThemeViewModel // Если используется в SettingsScreen напрямую

class MainActivity : ComponentActivity() {

    // Инициализируем ThemeViewModel на уровне Activity
    private val themeViewModel: ThemeViewModel by viewModels {
        ThemeViewModelFactory(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Убедитесь, что это не конфликтует с вашим подходом к теме/полноэкранному режиму

        setContent {
            // Собираем текущую тему из ThemeViewModel
            val currentAppTheme by themeViewModel.currentTheme.collectAsState()
            val useDarkTheme = when (currentAppTheme) {
                AppTheme.SYSTEM -> isSystemInDarkTheme()
                AppTheme.LIGHT -> false
                AppTheme.DARK -> true
            }

            // Применяем выбранную тему ко всему приложению
            SlavgorodBusTheme(darkTheme = useDarkTheme) {
                // Передаем themeViewModel в BusScheduleApp, если он нужен там для SettingsScreen
                BusScheduleApp(themeViewModel = themeViewModel)
            }
        }
    }
}

@Composable
fun BusScheduleApp(themeViewModel: ThemeViewModel) { // Принимаем ThemeViewModel
    val navController = rememberNavController()
    // BusViewModel остается локальным для BusScheduleApp, если он не нужен выше
    val busViewModel = remember { BusViewModel() }


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomNavigation(navController = navController)
        }
    ) { innerPadding ->
        AppNavHost(
            navController = navController,
            modifier = Modifier.padding(innerPadding),
            busViewModel = busViewModel,
            themeViewModel = themeViewModel // Передаем themeViewModel в NavHost
        )
    }
}

// Вынесем NavHost в отдельную Composable функцию для лучшей читаемости
@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    busViewModel: BusViewModel,
    themeViewModel: ThemeViewModel // Принимаем ThemeViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onRouteClick = { route ->
                    navController.navigate("schedule/${route.id}")
                },
                viewModel = busViewModel,
                navController = navController
            )
        }

        composable(Screen.Search.route) {
            SearchScreen(
                onRouteClick = { route ->
                    navController.navigate("schedule/${route.id}")
                },
                viewModel = busViewModel
            )
        }

        composable(Screen.FavoriteTimes.route) {
            FavoriteTimesScreen(
                viewModel = busViewModel
            )
        }

        composable(
            route = "schedule/{routeId}",
            arguments = listOf(
                navArgument("routeId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val routeId = backStackEntry.arguments?.getString("routeId")
            val route = busViewModel.getRouteById(routeId)
            ScheduleScreen(
                route = route,
                onBackClick = { navController.popBackStack() },
                viewModel = busViewModel
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen(
                themeViewModel = themeViewModel, // Если передаете явно
                onNavigateBack = { navController.popBackStack() } // <--- ПЕРЕДАЕМ КОЛЛБЭК
            )
        }

        composable(Screen.About.route) {
            AboutScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}