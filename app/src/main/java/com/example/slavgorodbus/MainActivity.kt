package com.example.slavgorodbus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.slavgorodbus.ui.navigation.BottomNavigation
// Убедитесь, что Screen импортируется корректно из вашего пакета навигации
import com.example.slavgorodbus.ui.navigation.Screen // <-- Важно!
import com.example.slavgorodbus.ui.screens.AboutScreen
import com.example.slavgorodbus.ui.screens.FavoriteTimesScreen
import com.example.slavgorodbus.ui.screens.HomeScreen
import com.example.slavgorodbus.ui.screens.ScheduleScreen
import com.example.slavgorodbus.ui.screens.SearchScreen
import com.example.slavgorodbus.ui.theme.SlavgorodBusTheme
import com.example.slavgorodbus.ui.viewmodel.BusViewModel

// УДАЛИТЕ ЭТИ СТРОКИ, ЕСЛИ ОНИ ЕСТЬ:
// private val Screen.About.route: Any
//     get() {
//         TODO()
//     }
// и любые другие подобные определения для Screen.Search.route и т.д.

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SlavgorodBusTheme {
                BusScheduleApp()
            }
        }
    }
}

@Composable
fun BusScheduleApp() {
    val navController = rememberNavController()
    val viewModel = remember { BusViewModel() }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomNavigation(navController = navController)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route, // Screen.Home.route здесь будет String
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    onRouteClick = { route ->
                        navController.navigate("schedule/${route.id}")
                    },
                    viewModel = viewModel,
                    navController = navController
                )
            }

            composable(Screen.Search.route) { // Screen.Search.route здесь будет String
                SearchScreen(
                    onRouteClick = { route ->
                        navController.navigate("schedule/${route.id}")
                    },
                    viewModel = viewModel
                )
            }

            composable(Screen.FavoriteTimes.route) { // Screen.FavoriteTimes.route здесь будет String
                FavoriteTimesScreen(
                    viewModel = viewModel
                )
            }

            composable(
                route = "schedule/{routeId}",
                arguments = listOf(
                    navArgument("routeId") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val routeId = backStackEntry.arguments?.getString("routeId")
                val route = viewModel.getRouteById(routeId)
                ScheduleScreen(
                    route = route,
                    onBackClick = { navController.popBackStack() },
                    viewModel = viewModel
                )
            }

            composable(Screen.About.route) { // Screen.About.route здесь будет String
                AboutScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}