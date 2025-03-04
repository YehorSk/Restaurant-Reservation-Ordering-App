package com.yehorsk.platea.core.domain.remote

data class RestaurantInfo(
    val name: String,
    val description: String,
    val address: String,
    val phone: String,
    val email: String,
    val website: String,
    val openingHours: String
)