package com.example.mobile.orders.presentation.create_order

sealed interface CreateOrderAction {

    data class UpdateRequest(val request: String): CreateOrderAction

    data class UpdateOrderType(val type: Int, val text: String): CreateOrderAction

    data class UpdateAddress(val address: String): CreateOrderAction

    data class UpdateInstructions(val instructions: String): CreateOrderAction

    data class UpdatePlace(val place: String): CreateOrderAction

    data object MakeOrder: CreateOrderAction



}