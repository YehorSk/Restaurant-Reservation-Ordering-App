package com.yehorsk.platea.orders.presentation

import com.yehorsk.platea.orders.data.remote.dto.TableDto
import com.yehorsk.platea.orders.domain.models.Order
import com.yehorsk.platea.orders.domain.models.OrderMenuItem
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDate

data class OrderUiState(
    val orderItems: List<OrderMenuItem> = emptyList(),
    val orders: List<Order> = emptyList(),
    val orderForm: OrderForm = OrderForm(),
    val isLoading: Boolean = false,
    val isLoadingPlaces: Boolean = false,
    val tables: List<TableDto>? = null,
    val showBottomSheet: Boolean = false,
    val countryCode: String = "",
    val phone: String = "",
    val isPhoneValid: Boolean = false
)

@Serializable
data class OrderForm(
    @SerialName("special_request")
    val specialRequest: String = "",
    @SerialName("order_type")
    val orderType: Int? = null,
    @SerialName("order_text")
    val orderText: String = "Pickup",
    val address: String = "",
    val instructions: String = "",
    @SerialName("table_id")
    val selectedTable: TableDto? = null,
    @SerialName("table_number")
    val selectedTableNumber:Int = 0,
    @SerialName("full_phone")
    val fullPhone: String = "",
    @SerialName("start_time")
    val startTime: String = "",
    @SerialName("end_time")
    val endTime: String = "",
    @SerialName("date")
    val selectedDate: String = LocalDate.now().toString()
)
