package com.yehorsk.platea.notifications.presentation

import com.yehorsk.platea.notifications.data.db.model.NotificationEntity

data class NotificationScreenUiState(
    val isLoading: Boolean = false,
    val currentNotification: NotificationEntity? = null
)
