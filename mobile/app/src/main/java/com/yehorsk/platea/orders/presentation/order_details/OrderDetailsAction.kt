package com.yehorsk.platea.orders.presentation.order_details

sealed interface OrderDetailsAction {

    data class RepeatOrder(val id: String): OrderDetailsAction

    data class UserCancelOrder(val id: String): OrderDetailsAction

    data class WaiterCancelOrder(val id: String): OrderDetailsAction

    data class CompleteOrder(val id: String): OrderDetailsAction

    data class ConfirmOrder(val id: String): OrderDetailsAction

    data class PrepareOrder(val id: String): OrderDetailsAction

    data class ReadyForPickupOrder(val id: String): OrderDetailsAction

}