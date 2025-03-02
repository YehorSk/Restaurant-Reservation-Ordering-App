package com.yehorsk.platea.reservations.presentation.reservations

import com.yehorsk.platea.core.domain.remote.ReservationFilter

sealed interface ReservationScreenAction {

    data object GetReservations: ReservationScreenAction

    data class OnSearchValueChange(val value: String): ReservationScreenAction

    data class UpdateFilter(val filter: ReservationFilter): ReservationScreenAction

}