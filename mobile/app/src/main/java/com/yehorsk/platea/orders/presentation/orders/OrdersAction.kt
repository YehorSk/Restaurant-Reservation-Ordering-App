package com.yehorsk.platea.orders.presentation.orders

import com.yehorsk.platea.core.domain.remote.OrderFilter

sealed interface OrdersAction {

    data class UpdateFilter(val filter: OrderFilter): OrdersAction

    data class UpdateSearch(val search: String): OrdersAction

    data object GetUserOrders: OrdersAction

}