package com.example.slavgorodbus.data.model

data class BusSchedule(
    val id: String,
    val routeId: String,
    val stopName: String,
    val departureTime: String,
    val arrivalTime: String,
    val dayOfWeek: Int,
    val isWeekend: Boolean = false
)

data class ScheduleStop(
    val id: String,
    val name: String,
    val location: String,
    val latitude: Double? = null,
    val longitude: Double? = null
)
