package com.yehorsk.platea.orders.domain.models

data class TimeSlot(
    val id: Int,
    val startTime: String,
    val endTime: String,
    val availableTables: Int
)
