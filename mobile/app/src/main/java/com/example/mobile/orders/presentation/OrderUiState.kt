package com.example.mobile.orders.presentation

import com.example.mobile.orders.data.db.model.OrderEntity
import com.example.mobile.orders.data.remote.dto.GooglePredictionDto
import com.example.mobile.orders.data.remote.dto.OrderMenuItemDto
import com.example.mobile.orders.data.remote.dto.TableDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

data class OrderUiState(
    val orderItems: List<OrderMenuItemDto>? = null,
    val orders: List<OrderEntity>? = null,
    val orderForm: OrderForm = OrderForm(),
    val isLoading: Boolean = false,
    val isLoadingPlaces: Boolean = false,
    val tables: List<TableDto>? = null,
    val places: List<GooglePredictionDto>? = null,
    val showBottomSheet: Boolean = false,
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
    val selectedTable: TableDto = TableDto(
        id = "1",
        createdAt = "2024-12-22 12:40:32",
        updatedAt = "2024-12-22 12:40:32",
        number = "1",
        capacity = 4
    ),
    @SerialName("table_number")
    val selectedTableNumber:Int = 0,
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
