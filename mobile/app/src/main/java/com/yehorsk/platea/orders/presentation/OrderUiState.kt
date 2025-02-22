package com.yehorsk.platea.orders.presentation

import com.yehorsk.platea.orders.data.db.model.OrderEntity
import com.yehorsk.platea.orders.data.remote.dto.GooglePredictionDto
import com.yehorsk.platea.orders.data.remote.dto.OrderMenuItemDto
import com.yehorsk.platea.orders.data.remote.dto.TableDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class OrderUiState(
    val orderItems: List<OrderMenuItemDto>? = null,
    val orders: List<OrderEntity>? = null,
    val orderForm: OrderForm = OrderForm(),
    val isLoading: Boolean = false,
    val isLoadingPlaces: Boolean = false,
    val tables: List<TableDto>? = null,
    val showBottomSheet: Boolean = false,
    val countryCode: String = "",
    val phone: String = "",
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
    val selectedTable: TableDto? = null,
    @SerialName("table_number")
    val selectedTableNumber:Int = 0,
    @SerialName("full_phone")
    val fullPhone: String = "",
    @SerialName("start_time")
    val startTime: String = getStartTime(),
    @SerialName("end_time")
    val endTime: String = getEndTime()
)

fun getStartTime(): String {
    val currentTime = LocalDateTime.now()
    return if (currentTime.hour >= 20) {
        "08:00"
    } else {
        val minute = currentTime.minute
        val roundedMinute = when {
            minute < 15 -> 0
            minute < 30 -> 15
            minute < 45 -> 30
            else -> 45
        }
        val roundedTime = currentTime.withMinute(roundedMinute).withSecond(0).withNano(0)
        formatTime(roundedTime)
    }
}

fun getEndTime(): String {
    val startTime = if (LocalDateTime.now().hour >= 20) {
        LocalDateTime.now().withHour(8).withMinute(30).withSecond(0).withNano(0)
    } else {
        val minute = LocalDateTime.now().minute
        val roundedMinute = getRoundedMinute(minute)
        LocalDateTime.now()
            .withMinute(roundedMinute)
            .withSecond(0)
            .withNano(0)
            .plusMinutes(30)
    }
    return formatTime(startTime)
}

fun getRoundedMinute(minute: Int): Int = when {
    minute < 15 -> 0
    minute < 30 -> 15
    minute < 45 -> 30
    else -> 45
}

fun formatTime(time: LocalDateTime): String {
    return time.format(DateTimeFormatter.ofPattern("HH:mm"))
}
