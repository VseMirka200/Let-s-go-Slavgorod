package com.example.slavgorodbus.data.model

data class FavoriteTime(
    val id: String,
    val routeId: String,
    val departureTime: String,
    val arrivalTime: String,
    val stopName: String,
    val isActive: Boolean = true
)
