package com.example.mobile.orders.presentation

import com.example.mobile.orders.data.remote.dto.OrderMenuItemDto
import com.example.mobile.orders.data.remote.dto.TableDto
import com.example.mobile.orders.data.remote.dto.TimeSlotDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDate

data class CreateOrderUiState(
    val items: List<OrderMenuItemDto>? = null,
    val orderForm: OrderForm = OrderForm(),
    val isLoading: Boolean = false,
    val tables: List<TableDto>? = null,
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
    @SerialName("table_id")
    val selectedTable:Int = 0,
)
