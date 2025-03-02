package com.yehorsk.platea.reservations.presentation.create_reservation

import com.yehorsk.platea.orders.presentation.OrderForm

sealed interface CreateReservationAction {

    data object ClearForm: CreateReservationAction

    data object CreateReservation: CreateReservationAction

    data class UpdatePartySize(val size: Int): CreateReservationAction

    data class UpdateReservationDate(val date: String): CreateReservationAction

    data class ValidatePhoneNumber(val phone: String): CreateReservationAction

    data class UpdatePhone(val phone: String): CreateReservationAction

    data class UpdateTimeSlot(val id: Int, val time: String): CreateReservationAction

    data class UpdateWithOrder(val withOrder: Boolean, val form: OrderForm): CreateReservationAction

    data class UpdateSpecialRequest(val request: String): CreateReservationAction

}