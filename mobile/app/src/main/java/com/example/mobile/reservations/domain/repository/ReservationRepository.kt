package com.example.mobile.reservations.domain.repository

import com.example.mobile.core.domain.remote.AppError
import com.example.mobile.core.domain.remote.Result
import com.example.mobile.orders.data.remote.dto.TimeSlotDto
import com.example.mobile.reservations.data.remote.dto.ReservationDto
import com.example.mobile.reservations.presentation.reservation_details.Status
import com.example.mobile.reservations.presentation.reservations.ReservationForm

interface ReservationRepository {

    suspend fun getAvailableTimeSlots(reservationForm: ReservationForm) : Result<List<TimeSlotDto>, AppError>

    suspend fun createReservation(reservationForm: ReservationForm) : Result<List<ReservationDto>, AppError>

    suspend fun getUserReservations() : Result<List<ReservationDto>, AppError>

    suspend fun getUserReservationDetails(id: String) : Result<List<ReservationDto>, AppError>

    suspend fun updateReservation(id: String, status: Status) : Result<List<ReservationDto>, AppError>

    suspend fun cancelUserReservation(id: String) : Result<List<ReservationDto>, AppError>

}