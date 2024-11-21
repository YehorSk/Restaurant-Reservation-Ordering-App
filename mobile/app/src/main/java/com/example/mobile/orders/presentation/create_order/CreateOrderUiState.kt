package com.example.mobile.orders.presentation.create_order

import com.example.mobile.orders.data.remote.dto.OrderMenuItemDto

data class CreateOrderUiState(
    val orderType: Int = 0,
    val orderText: String = "Pickup",
    val request: String = "",
    val items: List<OrderMenuItemDto>? = null
)
