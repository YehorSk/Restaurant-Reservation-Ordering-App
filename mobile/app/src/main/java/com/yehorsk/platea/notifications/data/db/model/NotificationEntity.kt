package com.yehorsk.platea.notifications.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yehorsk.platea.notifications.data.remote.dto.NotificationDto

@Entity("notification_table")
data class NotificationEntity(
    @PrimaryKey
    val id: Int,
    @ColumnInfo("created_at")
    val createdAt: String,
    @ColumnInfo("updated_at")
    val updatedAt: String,
    @ColumnInfo("user_id")
    val userId: String,
    val title: String,
    val body: String,
    val read: Int
)

fun NotificationEntity.toNotificationDto(): NotificationDto{
    return NotificationDto(
        id = this.id,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        userId = this.userId,
        title = this.title,
        body = this.body,
        read = this.read
    )
}