package com.example.mobile.orders.presentation.create_order

import com.example.mobile.orders.data.db.model.OrderEntity
import com.example.mobile.orders.data.remote.dto.OrderMenuItemDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class CreateOrderUiState(
    val items: List<OrderMenuItemDto>? = null,
    val selectedItem: OrderEntity? = null,
    val orderForm: OrderForm = OrderForm(),
    val isLoading: Boolean = false
)

@Serializable
data class OrderForm(
    @SerialName("special_request")
    val specialRequest: String = "",
    @SerialName("order_type")
    val orderType: Int = 0,
    @SerialName("order_text")
    val orderText: String = "Pickup",
    val address: String = "",
    val instructions: String = "",
)
