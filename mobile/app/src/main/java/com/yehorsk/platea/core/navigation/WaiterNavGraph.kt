package com.yehorsk.platea.core.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
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

@Composable
fun WaiterNavGraph(
    modifier:Modifier = Modifier,
    navController: NavHostController,
    onLoggedOut: () -> Unit
){

    NavHost(
        navController = navController,
        route = Graph.WAITER,
        startDestination = Screen.Home.route
    ){
        composable(
            route = Screen.Home.route,

        ) {
            val menuScreenViewModel = it.sharedHiltViewModel<MenuScreenViewModel>(navController)

            MenuScreenRoot(
                modifier = modifier,
                onSearchClicked = {
                    navController.navigate(Screen.Search.route)
                },
                onCreateReservationClicked = {},
                viewModel = menuScreenViewModel,
                isUser = false
            )
        }
        composable(
            route = Screen.Account.route,

        ) {
            val settingsViewModel = it.sharedHiltViewModel<SettingsViewModel>(navController)

            MainSettingsScreen(
                modifier = modifier.fillMaxSize(),
                viewModel = settingsViewModel,
                onNavigate = { destination ->
                    when(destination){
                        ProfileDestination.Favorites -> navController.navigate(Screen.Favorites.route)
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
            val settingsViewModel = it.sharedHiltViewModel<SettingsViewModel>(navController)

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
                showGoBack = true
            )
        }
        composable(
            route = Screen.Search.route,

        ) {
            val menuScreenViewModel = it.sharedHiltViewModel<MenuScreenViewModel>(navController)

            SearchScreen(
                modifier = modifier,
                onGoBack = {
                    navController.navigate(Screen.Home.route){
                        popUpTo(Screen.Home.route){
                            inclusive = true
                        }
                    }
                },
                viewModel = menuScreenViewModel
            )
        }
        composable(
            route = Screen.Cart.route,

        ) {
            val cartScreenViewModel: CartScreenViewModel = hiltViewModel()

            CartScreenRoot(
                modifier = modifier,
                viewModel = cartScreenViewModel,
                onGoToCheckoutClick = {
                    navController.navigate(Screen.CreateOrder.route)
                }
            )
        }
        composable(
            route = Screen.Favorites.route,

        ) {
            val menuScreenViewModel = it.sharedHiltViewModel<MenuScreenViewModel>(navController)

            FavoritesScreen(
                modifier = modifier,
                viewModel = menuScreenViewModel,
                onGoBack = { navController.popBackStack() },
                showGoBack = false
            )
        }
        composable(
            route = Screen.CreateOrder.route,

        ) {
            CreateOrderScreenRoot(
                modifier = modifier,
                onGoToCart = {
                    navController.navigate(Screen.Cart.route){
                        popUpTo(Screen.Cart.route){
                            inclusive = true
                        }
                    }
                },
                onGoToMenu = {
                    navController.navigate(Screen.Home.route){
                        popUpTo(Screen.Home.route){
                            inclusive = true
                        }
                    }
                },
                onGoToMakeReservation = {},
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
                showGoBack = true
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