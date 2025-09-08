package com.yehorsk.platea.reservations.presentation.reservation_details

import com.yehorsk.platea.reservations.domain.models.Reservation

data class ReservationDetailsUiState(
    val currentReservation: Reservation? = null,
    val isLoading: Boolean = false,
    val isNetwork: Boolean = false,
    val userRole: String? = null
)
