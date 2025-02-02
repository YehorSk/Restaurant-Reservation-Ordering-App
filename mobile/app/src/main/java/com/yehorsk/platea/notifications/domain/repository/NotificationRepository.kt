package com.yehorsk.platea.notifications.domain.repository

import com.yehorsk.platea.core.data.remote.dto.ResponseDto
import com.yehorsk.platea.core.domain.remote.AppError
import com.yehorsk.platea.core.domain.remote.Result
import com.yehorsk.platea.notifications.data.remote.dto.NotificationDto

interface NotificationRepository {

    suspend fun getAllNotifications(): Result<List<NotificationDto>, AppError>

    suspend fun read(id: String): Result<List<NotificationDto>, AppError>

}