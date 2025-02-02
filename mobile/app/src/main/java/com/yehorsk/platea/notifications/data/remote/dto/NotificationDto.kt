package com.yehorsk.platea.notifications.data.remote.dto

import com.yehorsk.platea.notifications.data.db.model.NotificationEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NotificationDto(
    val id: Int,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String,
    @SerialName("user_id")
    val userId: String,
    val title: String,
    val body: String,
    val read: Int
)

fun NotificationDto.toNotificationEntity(): NotificationEntity{
    return NotificationEntity(
        id = this.id,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        userId = this.userId,
        title = this.title,
        body = this.body,
        read = this.read
    )
}