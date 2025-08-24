package com.example.slavgorodbus.data.model

data class BusRoute(
    val id: String,
    val routeNumber: String,
    val name: String,
    val description: String,
    val isActive: Boolean = true,
    val isFavorite: Boolean = false,
    val color: String = "#1976D2",
    val pricePrimary: String? = null,
    val priceSecondary: String? = null,
    val directionDetails: String? = null,
    val travelTime: String?,
    val paymentMethods: String?
)