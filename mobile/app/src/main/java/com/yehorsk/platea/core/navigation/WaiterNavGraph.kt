package com.yehorsk.platea.core.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.yehorsk.platea.cart.presentation.cart.CartScreenRoot
import com.yehorsk.platea.cart.presentation.cart.CartScreenViewModel
import com.yehorsk.platea.core.presentation.settings.ChangeLanguageScreen
import com.yehorsk.platea.core.presentation.settings.ChangeThemeScreen
import com.yehorsk.platea.core.presentation.settings.MainSettingsScreen
import com.yehorsk.platea.core.presentation.settings.ProfileDestination
import com.yehorsk.platea.core.presentation.settings.ProfileScreen
import com.yehorsk.platea.core.presentation.settings.RestaurantInfoScreenRoot
import com.yehorsk.platea.core.presentation.settings.SettingsViewModel
import com.yehorsk.platea.menu.presentation.menu.FavoritesScreen
import com.yehorsk.platea.menu.presentation.menu.MenuScreenRoot
import com.yehorsk.platea.menu.presentation.menu.MenuScreenViewModel
import com.yehorsk.platea.menu.presentation.menu.SearchScreen
import com.yehorsk.platea.orders.presentation.create_order.CreateOrderScreenRoot
import com.yehorsk.platea.orders.presentation.order_details.OrderDetailsScreenRoot
import com.yehorsk.platea.orders.presentation.orders.OrdersScreen
import com.yehorsk.platea.reservations.presentation.reservation_details.ReservationDetailsScreenRoot
import com.yehorsk.platea.reservations.presentation.reservations.ReservationScreenRoot

fun NavGraphBuilder.waiterNavGraph(
    modifier:Modifier = Modifier,
    navController: NavHostController,
    onLoggedOut: () -> Unit
){

    navigation<Graph.Waiter>(
        startDestination = Screen.Home
    ){
        composable<Screen.Home> {
            val menuScreenViewModel = it.sharedHiltViewModel<MenuScreenViewModel>(navController)

            MenuScreenRoot(
                modifier = modifier,
                onSearchClicked = {
                    navController.navigate(Screen.Search)
                },
                onCreateReservationClicked = {},
                viewModel = menuScreenViewModel,
                isUser = false
            )
        }
        composable<Screen.Account> {
            val settingsViewModel = it.sharedHiltViewModel<SettingsViewModel>(navController)

            MainSettingsScreen(
                modifier = modifier.fillMaxSize(),
                viewModel = settingsViewModel,
                onNavigate = { destination ->
                    when(destination){
                        ProfileDestination.Favorites -> navController.navigate(Screen.Favorites)
                        ProfileDestination.Logout -> onLoggedOut()
                        ProfileDestination.Profile -> navController.navigate(Screen.Profile)
                        ProfileDestination.Orders -> navController.navigate(Screen.Orders)
                        ProfileDestination.Reservations -> navController.navigate(Screen.Reservations)
                        ProfileDestination.Language -> navController.navigate(Screen.Language)
                        ProfileDestination.Theme -> navController.navigate(Screen.Theme)
                        ProfileDestination.Info -> navController.navigate(Screen.Info)
                    }
                }
            )
        }
        composable<Screen.Info>{
            val settingsViewModel = it.sharedHiltViewModel<SettingsViewModel>(navController)

            RestaurantInfoScreenRoot(
                modifier = modifier,
                viewModel = settingsViewModel,
                onGoBack = { navController.popBackStack() }
            )
        }
        composable<Screen.Theme>{
            ChangeThemeScreen(
                modifier = modifier,
                onGoBack = { navController.popBackStack() }
            )
        }
        composable<Screen.Language>{
            ChangeLanguageScreen(
                modifier = modifier,
                onGoBack = { navController.popBackStack() }
            )
        }
        composable<Screen.Profile>{
            val settingsViewModel = it.sharedHiltViewModel<SettingsViewModel>(navController)

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
        composable<Screen.Orders>{
            OrdersScreen(
                modifier = modifier,
                onGoToOrderDetails = { id ->
                    navController.navigate(Screen.OrderDetails(id))
                },
                onGoBack = {
                    navController.popBackStack()
                },
                showGoBack = true
            )
        }
        composable<Screen.Search>{
            val menuScreenViewModel = it.sharedHiltViewModel<MenuScreenViewModel>(navController)

            SearchScreen(
                modifier = modifier,
                onGoBack = {
                    navController.navigate(Screen.Home){
                        popUpTo(Screen.Home){
                            inclusive = true
                        }
                    }
                },
                viewModel = menuScreenViewModel
            )
        }
        composable<Screen.Cart>{
            val cartScreenViewModel: CartScreenViewModel = hiltViewModel()

            CartScreenRoot(
                modifier = modifier,
                viewModel = cartScreenViewModel,
                onGoToCheckoutClick = {
                    navController.navigate(Screen.CreateOrder)
                }
            )
        }
        composable<Screen.Favorites>{
            val menuScreenViewModel = it.sharedHiltViewModel<MenuScreenViewModel>(navController)

            FavoritesScreen(
                modifier = modifier,
                viewModel = menuScreenViewModel,
                onGoBack = { navController.popBackStack() },
                showGoBack = false
            )
        }
        composable<Screen.CreateOrder>{
            CreateOrderScreenRoot(
                modifier = modifier,
                onGoToCart = {
                    navController.navigate(Screen.Cart){
                        popUpTo(Screen.Cart){
                            inclusive = true
                        }
                    }
                },
                onGoToMenu = {
                    navController.navigate(Screen.Home){
                        popUpTo(Screen.Home){
                            inclusive = true
                        }
                    }
                },
                onGoToMakeReservation = {},
            )
        }
        composable<Screen.OrderDetails>{
            val args = it.toRoute<Screen.OrderDetails>()
            OrderDetailsScreenRoot(
                modifier = modifier,
                onGoBack = {
                    navController.navigate(Screen.Orders){
                        popUpTo(Screen.Orders){
                            inclusive = true
                        }
                    }
                },
                id = args.id,
                onOpenItemDetails = {}
            )
        }
        composable<Screen.Reservations>{
            ReservationScreenRoot(
                modifier = modifier,
                onGoToReservationDetails = {id ->
                    navController.navigate(Screen.ReservationDetails(id))},
                onGoBack = {
                    navController.popBackStack()
                },
                showGoBack = true
            )
        }
        composable<Screen.ReservationDetails>{
            val args = it.toRoute<Screen.ReservationDetails>()
            ReservationDetailsScreenRoot(
                modifier = modifier,
                onGoBack = {
                    navController.navigate(Screen.Reservations){
                        popUpTo(Screen.Reservations){
                            inclusive = true
                        }
                    }
                },
                id = args.id
            )
        }
    }
}