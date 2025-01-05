package com.example.mobile.reservations.presentation.reservations

import com.example.mobile.orders.data.remote.dto.TimeSlotDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDate

data class ReservationUiState(
    val isLoading: Boolean = false,
    val timeSlots: List<TimeSlotDto>? = null,
    val reservationForm: ReservationForm = ReservationForm()
)

@Serializable
data class ReservationForm(
    @SerialName("party_size")
    val partySize:Int = 1,
    @SerialName("reservation_date")
    val reservationDate:String = LocalDate.now().toString(),
    @SerialName("time_slot_id")
    val selectedTimeSlot:Int = 0,
)