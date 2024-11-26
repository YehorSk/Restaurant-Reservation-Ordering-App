package com.example.mobile.orders.presentation.create_order

import com.example.mobile.orders.data.remote.dto.OrderMenuItemDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class CreateOrderUiState(
    val orderType: Int = 0,
    val orderText: String = "Pickup",
    val items: List<OrderMenuItemDto>? = null,
    val address: String = "",
    val instructions: String = "",
    val orderForm: OrderForm = OrderForm()
)

@Serializable
data class OrderForm(
    @SerialName("special_request")
    val specialRequest: String = "",
)
