package com.yehorsk.platea.notifications.data.remote

import com.yehorsk.platea.core.data.remote.safeCall
import com.yehorsk.platea.core.domain.remote.AppError
import com.yehorsk.platea.core.domain.remote.Result
import com.yehorsk.platea.notifications.data.dao.NotificationDao
import com.yehorsk.platea.notifications.data.remote.dto.NotificationDto
import com.yehorsk.platea.notifications.data.remote.dto.toNotificationEntity
import com.yehorsk.platea.notifications.domain.repository.NotificationRepository
import com.yehorsk.platea.notifications.domain.service.NotificationService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    val notificationService: NotificationService,
    val notificationDao: NotificationDao
) : NotificationRepository {

    suspend fun insert(items: List<NotificationDto>) = withContext(Dispatchers.IO) {
        for(item in items){
            notificationDao.insertNotification(item.toNotificationEntity())
        }
    }

    override suspend fun getAllNotifications(): Result<List<NotificationDto>, AppError> {
        Timber.d("Notification getAllNotifications")
        return safeCall<NotificationDto>(
            execute = {
                notificationService.getAllNotifications()
            },
            onSuccess = { data ->
                insert(data)
            }
        )
    }

    override suspend fun read(id: String): Result<List<NotificationDto>, AppError> {
        return safeCall<NotificationDto>(
            execute = {
                notificationService.read(id)
            }
        )
    }

}