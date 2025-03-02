package com.yehorsk.platea.reservations.data.remote.service

import com.yehorsk.platea.core.data.remote.dto.ResponseDto
import com.yehorsk.platea.orders.data.remote.dto.TimeSlotDto
import com.yehorsk.platea.reservations.data.remote.dto.ReservationDto
import com.yehorsk.platea.reservations.presentation.reservation_details.Status
import com.yehorsk.platea.reservations.presentation.reservations.ReservationForm
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ReservationService {

    @POST("reservation/user/getTimeSlots")
    suspend fun getAvailableTimeSlots(@Body reservationForm: ReservationForm) : ResponseDto<TimeSlotDto>

    @POST("reservation/user/createReservation")
    suspend fun createReservation(@Body reservationForm: ReservationForm) : ResponseDto<ReservationDto>

    @GET("reservation/user/reservations")
    suspend fun getReservations() : ResponseDto<ReservationDto>

    @GET("reservation/user/reservations/{id}")
    suspend fun getReservationDetails(@Path("id") id: String) : ResponseDto<ReservationDto>

    @PUT("reservation/admin/updateReservation/{id}")
    suspend fun updateReservation(@Path("id") id: String, @Body status: Status) : ResponseDto<ReservationDto>

    @GET("reservation/user/reservations/cancel/{id}")
    suspend fun cancelUserReservation(@Path("id") id: String) : ResponseDto<ReservationDto>

}