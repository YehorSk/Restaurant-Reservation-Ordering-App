package com.yehorsk.platea.orders.presentation.create_order

sealed interface CreateOrderAction {

    data class UpdateRequest(val request: String): CreateOrderAction

    data class UpdateOrderType(val type: Int, val text: String): CreateOrderAction

    data class UpdateAddress(val address: String): CreateOrderAction

    data class UpdateInstructions(val instructions: String): CreateOrderAction

    data class UpdateTime(val start: String, val end: String): CreateOrderAction

    data class UpdatePhone(val phone: String): CreateOrderAction

    data class ValidatePhone(val isValid: Boolean): CreateOrderAction

    data object MakeOrder: CreateOrderAction

    data object MakeWaiterOrder: CreateOrderAction

    data object CloseBottomSheet: CreateOrderAction

    data object OpenBottomSheet: CreateOrderAction

}