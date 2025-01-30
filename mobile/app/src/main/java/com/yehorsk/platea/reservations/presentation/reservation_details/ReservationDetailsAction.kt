package com.yehorsk.platea.reservations.presentation.reservation_details

import kotlinx.serialization.Serializable

@Serializable
data class Status(val status: String)

sealed interface ReservationDetailsAction {

    data class CancelReservation(val id: String): ReservationDetailsAction

    data class GetReservationDetails(val id: String): ReservationDetailsAction

    data class SetConfirmedStatus(val id: String, val status: Status = Status("Confirmed")): ReservationDetailsAction

    data class SetCancelledStatus(val id: String, val status: Status = Status("Cancelled")): ReservationDetailsAction

    data class SetPendingStatus(val id: String, val status: Status = Status("Pending")): ReservationDetailsAction

}