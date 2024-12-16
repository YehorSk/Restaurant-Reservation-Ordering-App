package com.example.mobile.orders.presentation.order_details

sealed interface OrderDetailsAction {

    data class RepeatOrder(val id: String): OrderDetailsAction

    data class CancelOrder(val id: String): OrderDetailsAction

}