package com.yehorsk.platea.reservations.domain.repository

import com.yehorsk.platea.core.domain.remote.AppError
import com.yehorsk.platea.core.domain.remote.Result
import com.yehorsk.platea.orders.data.remote.dto.TimeSlotDto
import com.yehorsk.platea.reservations.data.db.model.ReservationEntity
import com.yehorsk.platea.reservations.data.remote.dto.ReservationDto
import com.yehorsk.platea.reservations.domain.models.Reservation
import com.yehorsk.platea.reservations.presentation.reservation_details.Status
import com.yehorsk.platea.reservations.presentation.create_reservation.ReservationForm
import kotlinx.coroutines.flow.Flow

interface ReservationRepository {

    suspend fun getAvailableTimeSlots(reservationForm: ReservationForm) : Result<List<TimeSlotDto>, AppError>

    suspend fun createReservation(reservationForm: ReservationForm) : Result<List<ReservationDto>, AppError>

    suspend fun getUserReservations() : Result<List<Reservation>, AppError>

    suspend fun getMaxCapacity() : Result<List<Int>, AppError>

    suspend fun getUserReservationDetails(id: String) : Result<List<Reservation>, AppError>

    fun getUserReservationDetailsFlow(id: String) : Flow<Reservation>

    suspend fun updateReservation(id: String, status: Status) : Result<List<ReservationDto>, AppError>

    suspend fun cancelUserReservation(id: String) : Result<List<ReservationDto>, AppError>

    fun getUserReservationsFlow(): Flow<List<Reservation>>

}