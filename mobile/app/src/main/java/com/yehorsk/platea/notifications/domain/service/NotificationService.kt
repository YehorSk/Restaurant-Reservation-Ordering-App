package com.yehorsk.platea.notifications.domain.service

import com.yehorsk.platea.core.data.remote.dto.ResponseDto
import com.yehorsk.platea.notifications.data.remote.dto.NotificationDto
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface NotificationService {

    @GET("notifications/index")
    suspend fun getAllNotifications(): ResponseDto<NotificationDto>

    @FormUrlEncoded
    @PUT("notifications/read/{id}")
    suspend fun read(@Path("id") id: String): ResponseDto<NotificationDto>
}