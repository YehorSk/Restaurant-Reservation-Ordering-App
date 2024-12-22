package com.example.mobile.orders.presentation.create_order

import com.example.mobile.orders.data.remote.dto.OrderMenuItemDto
import com.example.mobile.orders.data.remote.dto.TableDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDate

data class CreateOrderUiState(
    val items: List<OrderMenuItemDto>? = null,
    val orderForm: OrderForm = OrderForm(),
    val isLoading: Boolean = false,
    val tables: List<TableDto>? = null
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
    @SerialName("party_size")
    val partySize:Int = 1,
    @SerialName("selected_table")
    val selectedTable:Int = 0,
    @SerialName("reservation_date")
    val reservationDate:String = LocalDate.now().toString()
)
