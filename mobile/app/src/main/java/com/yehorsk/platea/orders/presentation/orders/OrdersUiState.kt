package com.yehorsk.platea.orders.presentation.orders

import com.yehorsk.platea.core.utils.UiText
import com.yehorsk.platea.orders.domain.models.Order
import com.yehorsk.platea.orders.domain.models.SectionedOrders

data class OrdersUiState(
    val orders: Map<UiText, List<Order>> = emptyMap(),
    val isLoading: Boolean = false,
    val userRole: String? = null
    ){
    val sectionedOrders = orders
        .toList()
        .map { (date, orders) ->
            SectionedOrders(date, orders)
        }
}
