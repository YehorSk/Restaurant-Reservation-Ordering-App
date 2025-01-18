package com.example.mobile.core.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.mobile.core.presentation.settings.ChangeLanguageScreen
import com.example.mobile.core.presentation.settings.ChangeThemeScreen
import com.example.mobile.core.presentation.settings.MainSettingsScreen
import com.example.mobile.core.presentation.settings.ProfileDestination
import com.example.mobile.core.presentation.settings.ProfileScreen
import com.example.mobile.core.presentation.settings.SettingsViewModel
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
    val settingsViewModel: SettingsViewModel = hiltViewModel()
    NavHost(
        navController = navController,
        route = Graph.ADMIN,
        startDestination = AdminScreen.Orders.route
    ){
        composable(
            route = AdminScreen.Account.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            MainSettingsScreen(
                modifier = modifier.fillMaxSize(),
                viewModel = settingsViewModel,
                onNavigate = { destination ->
                    when(destination){
                        ProfileDestination.Favorites -> {}
                        ProfileDestination.Logout -> onLoggedOut()
                        ProfileDestination.Profile -> navController.navigate(AdminScreen.Profile.route)
                        ProfileDestination.Orders -> navController.navigate(AdminScreen.Orders.route)
                        ProfileDestination.Reservations -> navController.navigate(AdminScreen.Reservations.route)
                        ProfileDestination.Language -> navController.navigate(AdminScreen.Language.route)
                        ProfileDestination.Theme -> navController.navigate(AdminScreen.Theme.route)
                    }
                }
            )
        }
        composable(
            route = AdminScreen.Theme.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            ChangeThemeScreen(
                modifier = modifier,
                onGoBack = { navController.popBackStack() }
            )
        }
        composable(
            route = AdminScreen.Language.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            ChangeLanguageScreen(
                modifier = modifier,
                onGoBack = { navController.popBackStack() }
            )
        }
        composable(
            route = AdminScreen.Profile.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            ProfileScreen(
                modifier = modifier,
                onGoBack = {
                    navController.popBackStack()
                }
            )
        }
        composable(
            route = AdminScreen.Orders.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            OrdersScreen(
                modifier = modifier,
                onGoToOrderDetails = { id ->
                    navController.navigate(AdminScreen.OrderDetails(id))
                },
                onGoBack = {
                    navController.popBackStack()
                },
                showGoBack = false
            )
        }
        composable<AdminScreen.OrderDetails>(
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ){
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
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            ReservationScreenRoot(
                modifier = modifier,
                onGoToReservationDetails = {id ->
                    navController.navigate(AdminScreen.ReservationDetails(id))},
                onGoBack = {
                    navController.popBackStack()
                },
                showGoBack = false
            )
        }
        composable<AdminScreen.ReservationDetails>(
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ){
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
    object Account: AdminScreen(route = "ACCOUNT")
    data object Theme: AdminScreen(route = "THEME")
    data object Language: AdminScreen(route = "LANGUAGE")
}