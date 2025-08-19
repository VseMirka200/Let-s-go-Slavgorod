package com.example.slavgorodbus.data.model

data class BusRoute(
    val id: String,
    val routeNumber: String,
    val name: String,
    val description: String,
    val startStop: String,
    val endStop: String,
    val isActive: Boolean = true,
    val isFavorite: Boolean = false,
    val color: String = "#1976D2"
)
