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
import com.yehorsk.platea.cart.presentation.cart.CartScreenRoot
import com.yehorsk.platea.cart.presentation.cart.viewmodel.CartScreenViewModel
import com.yehorsk.platea.core.presentation.settings.ChangeLanguageScreen
import com.yehorsk.platea.core.presentation.settings.ChangeThemeScreen
import com.yehorsk.platea.core.presentation.settings.MainSettingsScreen
import com.yehorsk.platea.core.presentation.settings.ProfileDestination
import com.yehorsk.platea.core.presentation.settings.ProfileScreen
import com.yehorsk.platea.core.presentation.settings.RestaurantInfoScreenRoot
import com.yehorsk.platea.core.presentation.settings.SettingsViewModel
import com.yehorsk.platea.menu.presentation.favorites.FavoritesScreen
import com.yehorsk.platea.menu.presentation.menu.MenuScreenRoot
import com.yehorsk.platea.menu.presentation.menu.MenuScreenViewModel
import com.yehorsk.platea.menu.presentation.search.SearchScreen
import com.yehorsk.platea.orders.presentation.create_order.CreateOrderScreenRoot
import com.yehorsk.platea.orders.presentation.order_details.OrderDetailsScreenRoot
import com.yehorsk.platea.orders.presentation.orders.OrdersScreen
import com.yehorsk.platea.reservations.presentation.reservation_details.ReservationDetailsScreenRoot
import com.yehorsk.platea.reservations.presentation.reservations.ReservationScreenRoot
import kotlinx.serialization.Serializable

@Composable
fun WaiterNavGraph(
    modifier:Modifier = Modifier,
    navController: NavHostController,
    onLoggedOut: () -> Unit
){

    val menuScreenViewModel: MenuScreenViewModel = hiltViewModel()
    val cartScreenViewModel: CartScreenViewModel = hiltViewModel()
    val settingsViewModel: SettingsViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        route = Graph.WAITER,
        startDestination = WaiterScreen.Home.route
    ){
        composable(
            route = WaiterScreen.Home.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            MenuScreenRoot(
                modifier = modifier,
                onSearchClicked = {
                    navController.navigate(WaiterScreen.Search.route)
                },
                onCreateReservationClicked = {},
                viewModel = menuScreenViewModel,
                isUser = false
            )
        }
        composable(
            route = WaiterScreen.Account.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            MainSettingsScreen(
                modifier = modifier.fillMaxSize(),
                viewModel = settingsViewModel,
                onNavigate = { destination ->
                    when(destination){
                        ProfileDestination.Favorites -> navController.navigate(WaiterScreen.Favorites.route)
                        ProfileDestination.Logout -> onLoggedOut()
                        ProfileDestination.Profile -> navController.navigate(WaiterScreen.Profile.route)
                        ProfileDestination.Orders -> navController.navigate(WaiterScreen.Orders.route)
                        ProfileDestination.Reservations -> navController.navigate(WaiterScreen.Reservations.route)
                        ProfileDestination.Language -> navController.navigate(WaiterScreen.Language.route)
                        ProfileDestination.Theme -> navController.navigate(WaiterScreen.Theme.route)
                        ProfileDestination.Info -> navController.navigate(WaiterScreen.Info.route)
                    }
                }
            )
        }
        composable(
            route = WaiterScreen.Info.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            RestaurantInfoScreenRoot(
                modifier = modifier,
                viewModel = settingsViewModel,
                onGoBack = { navController.popBackStack() }
            )
        }
        composable(
            route = WaiterScreen.Theme.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            ChangeThemeScreen(
                modifier = modifier,
                onGoBack = { navController.popBackStack() }
            )
        }
        composable(
            route = WaiterScreen.Language.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            ChangeLanguageScreen(
                modifier = modifier,
                onGoBack = { navController.popBackStack() }
            )
        }
        composable(
            route = WaiterScreen.Profile.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
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
            route = WaiterScreen.Orders.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            OrdersScreen(
                modifier = modifier,
                onGoToOrderDetails = { id ->
                    navController.navigate(WaiterScreen.OrderDetails(id))
                },
                onGoBack = {
                    navController.popBackStack()
                },
                showGoBack = true
            )
        }
        composable(
            route = WaiterScreen.Search.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            SearchScreen(
                modifier = modifier,
                onGoBack = {
                    navController.navigate(WaiterScreen.Home.route){
                        popUpTo(WaiterScreen.Home.route){
                            inclusive = true
                        }
                    }
                },
                viewModel = menuScreenViewModel
            )
        }
        composable(
            route = WaiterScreen.Cart.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            CartScreenRoot(
                modifier = modifier,
                viewModel = cartScreenViewModel,
                onGoToCheckoutClick = {
                    navController.navigate(WaiterScreen.CreateOrder.route)
                }
            )
        }
        composable(
            route = WaiterScreen.Favorites.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            FavoritesScreen(
                modifier = modifier,
                viewModel = menuScreenViewModel,
                onGoBack = { navController.popBackStack() },
                showGoBack = false
            )
        }
        composable(
            route = WaiterScreen.CreateOrder.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            CreateOrderScreenRoot(
                modifier = modifier,
                onGoToCart = {
                    navController.navigate(WaiterScreen.Cart.route){
                        popUpTo(WaiterScreen.Cart.route){
                            inclusive = true
                        }
                    }
                },
                onGoToMenu = {
                    navController.navigate(WaiterScreen.Home.route){
                        popUpTo(WaiterScreen.Home.route){
                            inclusive = true
                        }
                    }
                },
                onGoToMakeReservation = {},
            )
        }
        composable<WaiterScreen.OrderDetails>(
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ){
            val args = it.toRoute<WaiterScreen.OrderDetails>()
            OrderDetailsScreenRoot(
                modifier = modifier,
                onGoBack = {
                    navController.navigate(WaiterScreen.Orders.route){
                        popUpTo(WaiterScreen.Orders.route){
                            inclusive = true
                        }
                    }
                },
                id = args.id,
                onOpenItemDetails = {}
            )
        }
        composable(
            route = WaiterScreen.Reservations.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            ReservationScreenRoot(
                modifier = modifier,
                onGoToReservationDetails = {id ->
                    navController.navigate(WaiterScreen.ReservationDetails(id))},
                onGoBack = {
                    navController.popBackStack()
                },
                showGoBack = true
            )
        }
        composable<WaiterScreen.ReservationDetails>(
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ){
            val args = it.toRoute<WaiterScreen.ReservationDetails>()
            ReservationDetailsScreenRoot(
                modifier = modifier,
                onGoBack = {
                    navController.navigate(WaiterScreen.Reservations.route){
                        popUpTo(WaiterScreen.Reservations.route){
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
sealed class WaiterScreen(val route: String){
    object Home: WaiterScreen(route = "HOME")
    object Account: WaiterScreen(route = "ACCOUNT")
    data object Profile: WaiterScreen(route = "PROFILE")
    object Cart: WaiterScreen(route = "CART")
    object Orders: WaiterScreen(route = "ORDERS")
    object CreateOrder: WaiterScreen(route = "CREATE_ORDER")
    data object Favorites: WaiterScreen(route = "FAVORITES")
    data object Search: WaiterScreen(route = "SEARCH")
    @Serializable
    data class OrderDetails(val id: Int): WaiterScreen(route = "ORDER_DETAILS")
    data object Reservations: WaiterScreen(route = "RESERVATIONS")
    @Serializable
    data class ReservationDetails(val id: Int): WaiterScreen(route = "RESERVATION_DETAILS")
    data object Theme: WaiterScreen(route = "THEME")
    data object Language: WaiterScreen(route = "LANGUAGE")
    data object Info: WaiterScreen(route = "INFO")
}