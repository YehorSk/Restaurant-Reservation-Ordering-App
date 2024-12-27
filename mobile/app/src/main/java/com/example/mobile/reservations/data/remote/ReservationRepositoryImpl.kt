package com.example.mobile.reservations.data.remote

import com.example.mobile.core.data.remote.safeCall
import com.example.mobile.core.domain.AppError
import com.example.mobile.core.domain.Result
import com.example.mobile.orders.data.remote.dto.TimeSlotDto
import com.example.mobile.reservations.data.dao.ReservationDao
import com.example.mobile.reservations.data.remote.dto.ReservationDto
import com.example.mobile.reservations.data.remote.dto.toReservationEntity
import com.example.mobile.reservations.domain.repository.ReservationRepository
import com.example.mobile.reservations.domain.service.ReservationService
import com.example.mobile.reservations.presentation.reservations.ReservationForm
import timber.log.Timber
import javax.inject.Inject

class ReservationRepositoryImpl @Inject constructor(
    private val reservationDao: ReservationDao,
    private val reservationService: ReservationService
) : ReservationRepository {

    override suspend fun getAvailableTimeSlots(reservationForm: ReservationForm): Result<List<TimeSlotDto>, AppError> {
        Timber.d("getAvailableTimeSlots")
        return safeCall<TimeSlotDto>(
            execute = {
                reservationService.getAvailableTimeSlots(reservationForm)
            }
        )
    }

    override suspend fun createReservation(reservationForm: ReservationForm): Result<List<ReservationDto>, AppError> {
        Timber.d("createReservation")
        return safeCall<ReservationDto>(
            execute = {
                reservationService.createReservation(reservationForm)
            },
            onSuccess = { data ->
                reservationDao.insertReservation(data.first().toReservationEntity())
            }
        )
    }

    override suspend fun getUserReservations(): Result<List<ReservationDto>, AppError> {
        Timber.d("getUserReservations")
        return safeCall<ReservationDto>(
            execute = {
                reservationService.getReservations()
            },
            onSuccess = { data ->
                reservationDao.insertReservations(data.map { it.toReservationEntity() })
            }
        )
    }

    override suspend fun getUserReservationDetails(id: String): Result<List<ReservationDto>, AppError> {
        Timber.d("getUserReservations")
        return safeCall<ReservationDto>(
            execute = {
                reservationService.getReservationDetails(id)
            },
            onSuccess = { data ->
                reservationDao.insertReservation(data.first().toReservationEntity())
            }
        )
    }

    override suspend fun cancelUserReservation(id: String): Result<List<ReservationDto>, AppError> {
        Timber.d("getUserReservations")
        return safeCall<ReservationDto>(
            execute = {
                reservationService.cancelUserReservation(id)
            },
            onSuccess = { data ->
                reservationDao.insertReservation(data.first().toReservationEntity())
            }
        )
    }

}