package com.example.mobile.core.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.mobile.core.presentation.settings.ProfileDestination
import com.example.mobile.core.presentation.settings.ProfileScreen
import com.example.mobile.core.presentation.settings.SettingsScreen
import com.example.mobile.orders.presentation.order_details.OrderDetailsScreenRoot
import com.example.mobile.orders.presentation.orders.OrdersScreen
import com.example.mobile.reservations.presentation.reservation_details.ReservationDetailsScreenRoot
import com.example.mobile.reservations.presentation.reservations.ReservationScreenRoot
import kotlinx.serialization.Serializable

@Composable
fun AdminNavGraph(
    modifier:Modifier = Modifier,
    navController: NavHostController,
    onLoggedOut: () -> Unit
){

    NavHost(
        navController = navController,
        route = Graph.ADMIN,
        startDestination = AdminScreen.Orders.route
    ){
        composable(AdminScreen.Settings.route) {
            SettingsScreen(
                modifier = modifier.fillMaxSize(),
                onNavigate = { destination ->
                    when(destination){
                        ProfileDestination.Favorites -> navController.navigate(ClientScreen.Favorites.route)
                        ProfileDestination.Logout -> onLoggedOut()
                        ProfileDestination.Profile -> navController.navigate(ClientScreen.Profile.route)
                    }
                }
            )
        }
        composable(AdminScreen.Profile.route) {
            ProfileScreen(
                modifier = modifier,
                onGoBack = {
                    navController.popBackStack()
                }
            )
        }
        composable(AdminScreen.Orders.route) {
            OrdersScreen(
                modifier = modifier,
                onGoToOrderDetails = { id ->
                    navController.navigate(AdminScreen.OrderDetails(id))
                }
            )
        }
        composable<AdminScreen.OrderDetails>{
            val args = it.toRoute<AdminScreen.OrderDetails>()
            OrderDetailsScreenRoot(
                modifier = modifier,
                onGoBack = {
                    navController.navigate(AdminScreen.Orders.route){
                        popUpTo(AdminScreen.Orders.route){
                            inclusive = true
                        }
                    }
                },
                id = args.id,
                onOpenItemDetails = {}
            )
        }
        composable(
            route = AdminScreen.Reservations.route,
        ) {
            ReservationScreenRoot(
                modifier = modifier,
                onGoToReservationDetails = {id ->
                    navController.navigate(AdminScreen.ReservationDetails(id))}
            )
        }
        composable<AdminScreen.ReservationDetails>{
            val args = it.toRoute<AdminScreen.ReservationDetails>()
            ReservationDetailsScreenRoot(
                modifier = modifier,
                onGoBack = {
                    navController.navigate(AdminScreen.Reservations.route){
                        popUpTo(AdminScreen.Reservations.route){
                            inclusive = true
                        }
                    }
                },
                id = args.id
            )
        }
    }
}

@Serializable
sealed class AdminScreen(val route: String){
    object Orders: AdminScreen(route = "ORDERS")
    @Serializable
    data class OrderDetails(val id: Int): AdminScreen(route = "ORDER_DETAILS")
    data object Reservations: AdminScreen(route = "RESERVATIONS")
    @Serializable
    data class ReservationDetails(val id: Int): AdminScreen(route = "RESERVATION_DETAILS")
    data object Profile: AdminScreen(route = "PROFILE")
    object Settings: AdminScreen(route = "SETTINGS")
}