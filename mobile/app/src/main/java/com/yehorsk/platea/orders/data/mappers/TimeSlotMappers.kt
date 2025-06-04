package com.yehorsk.platea.orders.data.mappers

import com.yehorsk.platea.orders.data.remote.dto.TimeSlotDto
import com.yehorsk.platea.orders.domain.models.TimeSlot

fun TimeSlotDto.toTimeSlot(): TimeSlot{
    return TimeSlot(
        id = id,
        startTime = startTime,
        endTime = endTime,
        availableTables = availableTables
    )
}