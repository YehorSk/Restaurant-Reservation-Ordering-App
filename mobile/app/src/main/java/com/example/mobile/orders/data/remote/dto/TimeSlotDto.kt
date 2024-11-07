package com.example.mobile.orders.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TimeSlotDto(
    val id: Int,
    @SerialName("start_time")
    val startTime: Long,
    @SerialName("end_time")
    val endTime: Long
)
