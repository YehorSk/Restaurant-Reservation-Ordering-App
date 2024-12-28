package com.example.mobile.orders.presentation

import com.example.mobile.orders.data.db.model.OrderEntity
import com.example.mobile.orders.data.db.model.OrderWithOrderItems
import com.example.mobile.orders.data.remote.dto.OrderMenuItemDto
import com.example.mobile.orders.data.remote.dto.TableDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class OrderUiState(
    val orderItems: List<OrderMenuItemDto>? = null,
    val orders: List<OrderEntity>? = null,
    val orderForm: OrderForm = OrderForm(),
    val isLoading: Boolean = false,
    val tables: List<TableDto>? = null,
)

enum class OrderFilter {
    ALL,
    COMPLETED,
    PENDING,
    CANCELLED
}

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
    @SerialName("table_id")
    val selectedTable:Int = 0,
    @SerialName("table_number")
    val selectedTableNumber:Int = 0,
)
