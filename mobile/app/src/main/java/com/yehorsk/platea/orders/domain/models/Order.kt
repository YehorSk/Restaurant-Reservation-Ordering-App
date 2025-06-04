package com.yehorsk.platea.orders.domain.models

data class Order(
    val id: String,
    val code: String,
    val createdAt: String,
    val updatedAt: String,
    val clientId: Int? = null,
    val tableId: Int? = null,
    val waiterId: Int? = null,
    val reservationId: Int? = null,
    val price: Double,
    val status: String,
    val phone: String? = null,
    val specialRequest: String? = null,
    val address: String? = null,
    val instructions: String? = null,
    val orderType: Int,
    val startTime: String,
    val endTime: String,
    val date: String? = null,
    val completedAt: String? = null,
    val orderItems: List<OrderMenuItem> = emptyList<OrderMenuItem>()
)
