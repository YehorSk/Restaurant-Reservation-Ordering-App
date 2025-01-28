package com.example.mobile.orders.presentation.create_order

import com.example.mobile.menu.presentation.MenuAction

sealed interface CreateOrderAction {

    data class UpdateRequest(val request: String): CreateOrderAction

    data class UpdateOrderType(val type: Int, val text: String): CreateOrderAction

    data class UpdateAddress(val address: String): CreateOrderAction

    data class UpdateInstructions(val instructions: String): CreateOrderAction

    data class UpdatePlace(val place: String): CreateOrderAction

    data class UpdateTime(val time: String): CreateOrderAction

    data object MakeOrder: CreateOrderAction

    data object MakeWaiterOrder: CreateOrderAction

    data object CloseBottomSheet: CreateOrderAction

    data object OpenBottomSheet: CreateOrderAction

}