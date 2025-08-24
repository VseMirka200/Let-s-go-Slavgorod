package com.example.slavgorodbus.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.slavgorodbus.data.model.BusRoute
import com.example.slavgorodbus.data.model.BusSchedule
import com.example.slavgorodbus.data.model.FavoriteTime
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class BusUiState(
    val routes: List<BusRoute> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class BusViewModel() : ViewModel() {

    private val _uiState = MutableStateFlow(BusUiState())
    val uiState: StateFlow<BusUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _favoriteTimes = MutableStateFlow<List<FavoriteTime>>(emptyList())
    val favoriteTimes: StateFlow<List<FavoriteTime>> = _favoriteTimes.asStateFlow()

    private val allRoutes = MutableStateFlow<List<BusRoute>>(emptyList())

    init {
        val sampleRoutes = listOf(
            BusRoute(
                id = "102",
                routeNumber = "102",
                name = "Автобус 102",
                description = "Маршрут между Славгородом и Яровым.",
                travelTime = "~40 минут.",
                pricePrimary = "35 руб. (по городу), 55 руб. (межгород)",
                paymentMethods = "Наличными, Банковской картой."
            )
            // Тут для других машрутов
        )
        allRoutes.value = sampleRoutes
        loadInitialRoutes()
    }

    private fun loadInitialRoutes() {
        _uiState.update { currentState ->
            currentState.copy(
                routes = allRoutes.value,
                isLoading = false,
                error = null
            )
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
        val routesToDisplay = if (query.isBlank()) {
            allRoutes.value
        } else {
            allRoutes.value.filter {
                it.routeNumber.contains(query, ignoreCase = true) ||
                        it.name.contains(query, ignoreCase = true)
            }
        }
        _uiState.update { currentState ->
            currentState.copy(
                routes = routesToDisplay,
            )
        }
    }

    fun getRouteById(routeId: String?): BusRoute? {
        if (routeId == null) return null
        return allRoutes.value.find { it.id == routeId }
    }

    fun addFavoriteTime(schedule: BusSchedule) {
        val favoriteTime = FavoriteTime(
            id = schedule.id,
            routeId = schedule.routeId,
            departureTime = schedule.departureTime,
            arrivalTime = schedule.arrivalTime,
            stopName = schedule.stopName
        )
        _favoriteTimes.update { currentFavorites ->
            if (currentFavorites.none { it.id == favoriteTime.id }) {
                currentFavorites + favoriteTime
            } else {
                currentFavorites
            }
        }
    }

    fun removeFavoriteTime(scheduleId: String) {
        _favoriteTimes.update { currentFavorites ->
            currentFavorites.filterNot { it.id == scheduleId }
        }
    }

    fun isFavoriteTime(scheduleId: String): Boolean {
        return favoriteTimes.value.any { it.id == scheduleId }
    }
}