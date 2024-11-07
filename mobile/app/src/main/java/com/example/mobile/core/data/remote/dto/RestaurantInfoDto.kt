package com.example.mobile.core.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RestaurantInfoDto(
    val id: Int,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String,
    val name: String,
    val description: String,
    val address: String,
    val phone: String,
    val email: String,
    val website: String,
    @SerialName("opening_hours")
    val openingHours: String
)