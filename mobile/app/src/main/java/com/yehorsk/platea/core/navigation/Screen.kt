package com.yehorsk.platea.core.navigation

import com.yehorsk.platea.orders.presentation.create_order.OrderForm
import kotlinx.serialization.Serializable

@Serializable
sealed class Screen(val route: String) {
    // 🔹 Auth
    @Serializable data object Login : Screen("LOGIN")
    @Serializable data object SignUp : Screen("SIGN_UP")
    @Serializable data object ForgotPwd : Screen("FORGOT_PWD")

    // 🔹 Common / Main
    @Serializable data object Home : Screen("HOME")
    @Serializable data object Cart : Screen("CART")
    @Serializable data object Orders : Screen("ORDERS")
    @Serializable data object CreateOrder : Screen("CREATE_ORDER")

    @Serializable
    data class CreateReservation(
        val withOrder: Boolean,
        val orderForm: OrderForm = OrderForm()
    ) : Screen("MAKE_RESERVATION")

    @Serializable data object ConfirmReservation : Screen("CONFIRM_RESERVATION")

    @Serializable
    data class OrderDetails(val id: Int) : Screen("ORDER_DETAILS")

    @Serializable data object Reservations : Screen("RESERVATIONS")

    @Serializable
    data class ReservationDetails(val id: Int) : Screen("RESERVATION_DETAILS")

    // 🔹 Shared screens
    @Serializable data object Account : Screen("ACCOUNT")
    @Serializable data object Profile : Screen("PROFILE")
    @Serializable data object Favorites : Screen("FAVORITES")
    @Serializable data object Search : Screen("SEARCH")
    @Serializable data object Theme : Screen("THEME")
    @Serializable data object Language : Screen("LANGUAGE")
    @Serializable data object Info : Screen("INFO")

    // 🔹 Extra
    @Serializable
    data class OrderItemDetails(val id: Int) : Screen("ORDER_ITEM_DETAILS")
}

