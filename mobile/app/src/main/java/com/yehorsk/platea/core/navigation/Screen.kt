package com.yehorsk.platea.core.navigation

import com.yehorsk.platea.orders.presentation.create_order.OrderForm
import kotlinx.serialization.Serializable

sealed interface Screen {
    @Serializable data object Login : Screen
    @Serializable data object SignUp : Screen
    @Serializable data object ForgotPwd : Screen

    @Serializable data object Home : Screen
    @Serializable data object Cart : Screen
    @Serializable data object Orders : Screen
    @Serializable data object CreateOrder : Screen

    @Serializable
    data class CreateReservation(
        val withOrder: Boolean,
        val orderForm: OrderForm = OrderForm()
    ) : Screen

    @Serializable data object ConfirmReservation : Screen

    @Serializable
    data class OrderDetails(val id: Int) : Screen

    @Serializable data object Reservations : Screen

    @Serializable
    data class ReservationDetails(val id: Int) : Screen

    @Serializable data object Account : Screen
    @Serializable data object Profile : Screen
    @Serializable data object Favorites : Screen
    @Serializable data object Search : Screen
    @Serializable data object Theme : Screen
    @Serializable data object Language : Screen
    @Serializable data object Info : Screen

    @Serializable
    data class OrderItemDetails(val id: Int) :Screen
}

