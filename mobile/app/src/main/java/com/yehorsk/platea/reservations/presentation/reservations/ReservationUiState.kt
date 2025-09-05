package com.yehorsk.platea.reservations.presentation.reservations

import com.yehorsk.platea.core.utils.UiText
import com.yehorsk.platea.orders.data.remote.dto.TimeSlotDto
import com.yehorsk.platea.orders.presentation.create_order.OrderForm
import com.yehorsk.platea.reservations.domain.models.Reservation
import com.yehorsk.platea.reservations.domain.models.SectionedReservations
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDate

data class ReservationUiState(
    val reservations: Map<UiText, List<Reservation>> = emptyMap(),
    val currentReservation: Reservation? = null,
    val isLoading: Boolean = false,
    val timeSlots: List<TimeSlotDto>? = null,
    val validPhoneNumber: Boolean = false,
    val reservationForm: ReservationForm = ReservationForm(),
    val countryCode: String = "",
    val phone: String = "",
    val isPhoneValid: Boolean = false,
    val maxCapacity: Int? = null
){
    val sectionedReservations = reservations
        .toList()
        .map { (date, reservations) ->
            SectionedReservations(date, reservations)
        }
}


@Serializable
data class ReservationForm(
    @SerialName("party_size")
    val partySize:Int = 1,
    @SerialName("reservation_date")
    val reservationDate:String = LocalDate.now().toString(),
    @SerialName("time_slot_id")
    val selectedTimeSlot:Int = 0,
    val selectedTime:String = "",
    @SerialName("special_request")
    val specialRequest: String = "",
    @SerialName("full_phone")
    val fullPhone: String = "",
    @SerialName("with_order")
    val withOrder: Boolean = false,
    @SerialName("order_form")
    val orderForm: OrderForm = OrderForm()
)