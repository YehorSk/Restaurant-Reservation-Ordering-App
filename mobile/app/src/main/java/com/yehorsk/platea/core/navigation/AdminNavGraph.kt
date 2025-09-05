package com.yehorsk.platea.core.navigation

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
import com.yehorsk.platea.R
import com.yehorsk.platea.core.presentation.settings.ChangeLanguageScreen
import com.yehorsk.platea.core.presentation.settings.ChangeThemeScreen
import com.yehorsk.platea.core.presentation.settings.MainSettingsScreen
import com.yehorsk.platea.core.presentation.settings.ProfileDestination
import com.yehorsk.platea.core.presentation.settings.ProfileScreen
import com.yehorsk.platea.core.presentation.settings.RestaurantInfoScreenRoot
import com.yehorsk.platea.core.presentation.settings.SettingsViewModel
import com.yehorsk.platea.orders.presentation.order_details.OrderDetailsScreenRoot
import com.yehorsk.platea.orders.presentation.orders.OrdersScreen
import com.yehorsk.platea.reservations.presentation.reservation_details.ReservationDetailsScreenRoot
import com.yehorsk.platea.reservations.presentation.reservations.ReservationScreenRoot
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
        startDestination = Screen.Orders.route
    ){
        composable(
            route = Screen.Account.route,

        ) {
            MainSettingsScreen(
                modifier = modifier.fillMaxSize(),
                viewModel = settingsViewModel,
                onNavigate = { destination ->
                    when(destination){
                        ProfileDestination.Favorites -> {}
                        ProfileDestination.Logout -> onLoggedOut()
                        ProfileDestination.Profile -> navController.navigate(Screen.Profile.route)
                        ProfileDestination.Orders -> navController.navigate(Screen.Orders.route)
                        ProfileDestination.Reservations -> navController.navigate(Screen.Reservations.route)
                        ProfileDestination.Language -> navController.navigate(Screen.Language.route)
                        ProfileDestination.Theme -> navController.navigate(Screen.Theme.route)
                        ProfileDestination.Info -> navController.navigate(Screen.Info.route)
                    }
                }
            )
        }
        composable(
            route = Screen.Info.route,

        ) {
            RestaurantInfoScreenRoot(
                modifier = modifier,
                viewModel = settingsViewModel,
                onGoBack = { navController.popBackStack() }
            )
        }
        composable(
            route = Screen.Theme.route,

        ) {
            ChangeThemeScreen(
                modifier = modifier,
                onGoBack = { navController.popBackStack() }
            )
        }
        composable(
            route = Screen.Language.route,

        ) {
            ChangeLanguageScreen(
                modifier = modifier,
                onGoBack = { navController.popBackStack() }
            )
        }
        composable(
            route = Screen.Profile.route,

        ) {
            ProfileScreen(
                modifier = modifier,
                viewModel = settingsViewModel,
                onGoBack = {
                    navController.popBackStack()
                },
                onDeleteAccount = {
                    onLoggedOut()
                }
            )
        }
        composable(
            route = Screen.Orders.route,

        ) {
            OrdersScreen(
                modifier = modifier,
                onGoToOrderDetails = { id ->
                    navController.navigate(Screen.OrderDetails(id))
                },
                onGoBack = {
                    navController.popBackStack()
                },
                showGoBack = false,
                title = R.string.orders,
            )
        }
        composable<Screen.OrderDetails>(

        ){
            val args = it.toRoute<Screen.OrderDetails>()
            OrderDetailsScreenRoot(
                modifier = modifier,
                onGoBack = {
                    navController.navigate(Screen.Orders.route){
                        popUpTo(Screen.Orders.route){
                            inclusive = true
                        }
                    }
                },
                id = args.id,
                onOpenItemDetails = {}
            )
        }
        composable(
            route = Screen.Reservations.route,

        ) {
            ReservationScreenRoot(
                modifier = modifier,
                onGoToReservationDetails = {id ->
                    navController.navigate(Screen.ReservationDetails(id))},
                onGoBack = {
                    navController.popBackStack()
                },
                showGoBack = false,
                title = R.string.reservations
            )
        }
        composable<Screen.ReservationDetails>(

        ){
            val args = it.toRoute<Screen.ReservationDetails>()
            ReservationDetailsScreenRoot(
                modifier = modifier,
                onGoBack = {
                    navController.navigate(Screen.Reservations.route){
                        popUpTo(Screen.Reservations.route){
                            inclusive = true
                        }
                    }
                },
                id = args.id
            )
        }
    }
}