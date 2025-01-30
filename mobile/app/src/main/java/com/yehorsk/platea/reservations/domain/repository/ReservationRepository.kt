package com.yehorsk.platea.reservations.domain.repository

import com.yehorsk.platea.core.domain.remote.AppError
import com.yehorsk.platea.core.domain.remote.Result
import com.yehorsk.platea.orders.data.remote.dto.TimeSlotDto
import com.yehorsk.platea.reservations.data.remote.dto.ReservationDto
import com.yehorsk.platea.reservations.presentation.reservation_details.Status
import com.yehorsk.platea.reservations.presentation.reservations.ReservationForm

interface ReservationRepository {

    suspend fun getAvailableTimeSlots(reservationForm: ReservationForm) : Result<List<TimeSlotDto>, AppError>

    suspend fun createReservation(reservationForm: ReservationForm) : Result<List<ReservationDto>, AppError>

    suspend fun getUserReservations() : Result<List<ReservationDto>, AppError>

    suspend fun getUserReservationDetails(id: String) : Result<List<ReservationDto>, AppError>

    suspend fun updateReservation(id: String, status: Status) : Result<List<ReservationDto>, AppError>

    suspend fun cancelUserReservation(id: String) : Result<List<ReservationDto>, AppError>

}