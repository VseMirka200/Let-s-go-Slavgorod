// В файле com/example/slavgorodbus/data/model/BusRoute.kt
package com.example.slavgorodbus.data.model

data class BusRoute(
    val id: String,
    val routeNumber: String,
    val name: String,
    val description: String,
    val isActive: Boolean = true,
    val isFavorite: Boolean = false,
    val color: String = "#1976D2", // Убедитесь, что это поле есть и используется
    val pricePrimary: String? = null,
    val priceSecondary: String? = null,
    val directionDetails: String? = null
    // Поля startStop и endStop могут быть здесь, если они нужны для других целей,
    // но в карточке мы их не отображаем
)