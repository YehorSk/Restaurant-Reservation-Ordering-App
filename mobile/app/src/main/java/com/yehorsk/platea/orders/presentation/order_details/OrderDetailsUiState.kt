package com.yehorsk.platea.orders.presentation.order_details

import com.yehorsk.platea.orders.domain.models.Order

data class OrderDetailsUiState(
    val currentOrder: Order? = null,
    val isLoading: Boolean = false,
    val isNetwork: Boolean = false,
    val userRole: String? = null
)
