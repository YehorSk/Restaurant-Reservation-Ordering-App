package com.yehorsk.platea.notifications.presentation

sealed interface NotificationAction{

    data class MarkAsRead(val id: String): NotificationAction

    data class ShowNotificationDetails(val id: String): NotificationAction

}