package com.yehorsk.platea.reservations.presentation.reservations

import com.yehorsk.platea.core.utils.UiText
import com.yehorsk.platea.reservations.domain.models.Reservation
import com.yehorsk.platea.reservations.domain.models.SectionedReservations

data class ReservationUiState(
    val reservations: Map<UiText, List<Reservation>> = emptyMap(),
    val isLoading: Boolean = false,
    val userRole: String? = null
){
    val sectionedReservations = reservations
        .toList()
        .map { (date, reservations) ->
            SectionedReservations(date, reservations)
        }
}