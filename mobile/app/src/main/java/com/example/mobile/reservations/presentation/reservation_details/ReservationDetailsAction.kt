package com.example.mobile.reservations.presentation.reservation_details

sealed interface ReservationDetailsAction {

    data class CancelReservation(val id: String): ReservationDetailsAction

    data class GetReservationDetails(val id: String): ReservationDetailsAction

}