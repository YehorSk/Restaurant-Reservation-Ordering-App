package com.yehorsk.platea.reservations.presentation.create_reservation

import com.yehorsk.platea.core.data.db.models.RestaurantInfoEntity
import com.yehorsk.platea.orders.data.remote.dto.TimeSlotDto
import com.yehorsk.platea.orders.presentation.create_order.OrderForm
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDate

data class CreateReservationUiState(
    val isLoading: Boolean = false,
    val isNetwork: Boolean = false,
    val restaurantInfo: RestaurantInfoEntity? = null,
    val countryCode: String = "",
    val phone: String = "",
    val reservationForm: ReservationForm = ReservationForm(),
    val isPhoneValid: Boolean = false,
    val timeSlots: List<TimeSlotDto>? = null,
    val maxCapacity: Int? = null
)

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