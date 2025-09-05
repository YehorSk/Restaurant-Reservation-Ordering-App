package com.yehorsk.platea.reservations.data.remote

import com.yehorsk.platea.core.data.remote.service.safeCall
import com.yehorsk.platea.core.domain.remote.AppError
import com.yehorsk.platea.core.domain.remote.Result
import com.yehorsk.platea.core.domain.remote.map
import com.yehorsk.platea.core.domain.remote.onSuccess
import com.yehorsk.platea.orders.data.dao.OrderDao
import com.yehorsk.platea.orders.data.remote.dto.TimeSlotDto
import com.yehorsk.platea.reservations.data.dao.ReservationDao
import com.yehorsk.platea.reservations.data.db.model.ReservationEntity
import com.yehorsk.platea.reservations.data.mappers.toReservation
import com.yehorsk.platea.reservations.data.remote.dto.ReservationDto
import com.yehorsk.platea.reservations.data.remote.dto.toReservationEntity
import com.yehorsk.platea.reservations.data.remote.service.ReservationService
import com.yehorsk.platea.reservations.domain.models.Reservation
import com.yehorsk.platea.reservations.domain.repository.ReservationRepository
import com.yehorsk.platea.reservations.presentation.reservation_details.Status
import com.yehorsk.platea.reservations.presentation.reservations.ReservationForm
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject
import kotlin.collections.map

class ReservationRepositoryImpl @Inject constructor(
    private val reservationDao: ReservationDao,
    private val orderDao: OrderDao,
    private val reservationService: ReservationService
) : ReservationRepository {

    private suspend fun syncUserReservationsWithServer(data: List<ReservationDto>){
        val localItems = reservationDao.getUserReservationsOnce()
        val serverItemIds = data.map { it.id }.toSet()
        val itemsToDelete = localItems.filter { it.id !in serverItemIds }

        reservationDao.runInTransaction {
            reservationDao.insertReservations(data.map { it.toReservationEntity() })
            reservationDao.deleteItems(itemsToDelete)
        }
    }

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
            }
        ).onSuccess { data, _ ->
            reservationDao.insertReservation(data.first().toReservationEntity())
            if(reservationForm.withOrder){
                orderDao.deleteAllCartItems()
            }
        }
    }

    override suspend fun getUserReservations(): Result<List<Reservation>, AppError> {
        Timber.d("getUserReservations")
        return safeCall<ReservationDto>(
            execute = {
                reservationService.getReservations()
            }
        ).onSuccess { data, _ ->
            syncUserReservationsWithServer(data)
        }.map { data ->
            data.map {
                it.toReservation()
            }
        }
    }

    override suspend fun getMaxCapacity(): Result<List<Int>, AppError> {
        Timber.d("getMaxCapacity")
        return safeCall<Int>(
            execute = {
                reservationService.getMaxCapacity()
            }
        )
    }

    override suspend fun getUserReservationDetails(id: String): Result<List<Reservation>, AppError> {
        Timber.d("getUserReservations")
        return safeCall<ReservationDto>(
            execute = {
                reservationService.getReservationDetails(id)
            }
        ).onSuccess { data, _ ->
            reservationDao.insertReservation(data.first().toReservationEntity())
        }.map { data ->
            data.map {
                it.toReservation()
            }
        }
    }

    override suspend fun updateReservation(
        id: String,
        status: Status
    ): Result<List<ReservationDto>, AppError> {
        Timber.d("updateReservation")
        return safeCall<ReservationDto>(
            execute = {
                reservationService.updateReservation(id, status)
            }
        ).onSuccess { data, _ ->
            reservationDao.insertReservation(data.first().toReservationEntity())
        }
    }

    override suspend fun cancelUserReservation(id: String): Result<List<ReservationDto>, AppError> {
        Timber.d("cancelUserReservation")
        return safeCall<ReservationDto>(
            execute = {
                reservationService.cancelUserReservation(id)
            }
        ).onSuccess { data, _ ->
            reservationDao.insertReservation(data.first().toReservationEntity())
        }
    }

    override fun getUserReservationsFlow(): Flow<List<Reservation>> {
        return reservationDao.getUserReservations().map { data ->
            data.map {
                it.toReservation()
            }
        }
    }

}