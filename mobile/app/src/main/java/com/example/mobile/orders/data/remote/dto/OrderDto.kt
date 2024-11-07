package com.example.mobile.orders.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OrderDto(
    val id: String,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String,
    @SerialName("client_id")
    val clientId: Int,
    @SerialName("table_id")
    val tableId: Int,
    @SerialName("waiter_id")
    val waiterId: Int,
    @SerialName("reservation_id")
    val reservationId: Int,
    val price: Double,
    val status: String,
    @SerialName("special_request")
    val specialRequest: String,
    val pickup: Boolean,
    @SerialName("home_delivery")
    val homeDelivery: Boolean,
    @SerialName("dine_in")
    val dineIn: Boolean
)
