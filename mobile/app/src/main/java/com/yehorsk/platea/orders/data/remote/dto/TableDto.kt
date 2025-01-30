package com.yehorsk.platea.orders.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TableDto(
    val id: String,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String,
    val number: String,
    val capacity: Int
)
