package com.example.mobile.core.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.mobile.cart.presentation.cart.CartScreenRoot
import com.example.mobile.cart.presentation.cart.viewmodel.CartScreenViewModel
import com.example.mobile.core.presentation.settings.ProfileDestination
import com.example.mobile.core.presentation.settings.ProfileScreen
import com.example.mobile.menu.presentation.menu.MenuScreenRoot
import com.example.mobile.orders.presentation.orders.OrdersScreen
import com.example.mobile.core.presentation.settings.AccountScreen
import com.example.mobile.menu.presentation.favorites.FavoritesScreen
import com.example.mobile.menu.presentation.menu.viewmodel.MenuScreenViewModel
import com.example.mobile.menu.presentation.search.SearchScreen
import com.example.mobile.orders.presentation.create_order.user.UserCreateOrderScreenRoot
import com.example.mobile.reservations.presentation.create_reservation.CreateReservationScreen
import com.example.mobile.orders.presentation.order_details.OrderDetailsScreenRoot
import com.example.mobile.reservations.presentation.reservation_details.ReservationDetailsScreenRoot
import com.example.mobile.reservations.presentation.reservations.ReservationScreenRoot
import kotlinx.serialization.Serializable

@Composable
fun ClientNavGraph(
    navController: NavHostController,
    modifier: Modifier,
    onLoggedOut: () -> Unit
){
    val menuScreenViewModel: MenuScreenViewModel = hiltViewModel()
//    val cartScreenViewModel: CartScreenViewModel = hiltViewModel()
    val ordersScreenViewModel: CartScreenViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        route = Graph.HOME,
        startDestination = ClientScreen.Home.route
    ){
        composable(
            route = ClientScreen.Home.route,
        ) {
            MenuScreenRoot(
                modifier = modifier,
                onSearchClicked = {
                    navController.navigate(ClientScreen.Search.route)
                },
                onCreateReservationClicked = {
                    navController.navigate(ClientScreen.CreateReservation.route)
                },
                viewModel = menuScreenViewModel,
                isUser = true
            )
        }
        composable(ClientScreen.Account.route) {
            AccountScreen(
                modifier = modifier.fillMaxSize(),
                onNavigate = { destination ->
                    when(destination){
                        ProfileDestination.Favorites -> navController.navigate(ClientScreen.Favorites.route)
                        ProfileDestination.Logout -> onLoggedOut()
                        ProfileDestination.Profile -> navController.navigate(ClientScreen.Profile.route)
                        ProfileDestination.Orders -> navController.navigate(ClientScreen.Orders.route)
                        ProfileDestination.Reservations -> navController.navigate(ClientScreen.Reservations.route)
                    }
                }
            )
        }
        composable(ClientScreen.Profile.route) {
            ProfileScreen(
                modifier = modifier,
                onGoBack = {
                    navController.popBackStack()
                }
            )
        }
        composable(ClientScreen.Orders.route) {
            OrdersScreen(
                modifier = modifier,
                onGoToOrderDetails = { id ->
                    navController.navigate(ClientScreen.OrderDetails(id))
                },
                onGoBack = {
                    navController.popBackStack()
                },
                showGoBack = true
            )
        }
        composable(
            route = ClientScreen.CreateReservation.route,
            enterTransition =   { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(700)) },
            exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(700)) },
            popEnterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(700)) },
            popExitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(700)) }
            ) {
            CreateReservationScreen(
                modifier = modifier,
                goBack = {
                    navController.popBackStack()
                }
            )
        }
        composable(ClientScreen.CreateOrder.route) {
            UserCreateOrderScreenRoot(
                modifier = modifier,
                onGoToCart = {
                    navController.navigate(ClientScreen.Cart.route){
                        popUpTo(ClientScreen.Cart.route){
                            inclusive = true
                        }
                    }
                },
                onGoToMenu = {
                    navController.navigate(ClientScreen.Home.route){
                        popUpTo(ClientScreen.Home.route){
                            inclusive = true
                        }
                    }
                },
                onGoToOrders = {
                    navController.navigate(ClientScreen.Orders.route){
                        popUpTo(ClientScreen.Orders.route){
                            inclusive = true
                        }
                    }
                },
                onGoToMakeReservation = {
                    navController.navigate(ClientScreen.CreateReservation.route){}
                }
            )
        }
        composable<ClientScreen.OrderDetails>{
            val args = it.toRoute<ClientScreen.OrderDetails>()
            OrderDetailsScreenRoot(
                modifier = modifier,
                onGoBack = {
                    navController.navigate(ClientScreen.Orders.route){
                        popUpTo(ClientScreen.Orders.route){
                            inclusive = true
                        }
                    }
                },
                id = args.id,
                onOpenItemDetails = {}
            )
        }
        composable<ClientScreen.ReservationDetails>{
            val args = it.toRoute<ClientScreen.ReservationDetails>()
            ReservationDetailsScreenRoot(
                modifier = modifier,
                onGoBack = {
                    navController.navigate(ClientScreen.Reservations.route){
                        popUpTo(ClientScreen.Reservations.route){
                            inclusive = true
                        }
                    }
                },
                id = args.id
            )
        }
        composable(ClientScreen.Favorites.route) {
            FavoritesScreen(
                modifier = modifier,
                viewModel = menuScreenViewModel,
                onGoBack = { navController.popBackStack() }
            )
        }
        composable(
            route = ClientScreen.Cart.route,
        ) {
            CartScreenRoot(
                modifier = modifier,
                onGoToCheckoutClick = { navController.navigate(ClientScreen.CreateOrder.route) }
            )
        }
        composable(
            route = ClientScreen.Reservations.route,
        ) {
            ReservationScreenRoot(
                modifier = modifier,
                onGoToReservationDetails = {id ->
                    navController.navigate(ClientScreen.ReservationDetails(id))},
                onGoBack = { navController.popBackStack() },
                showGoBack = true
            )
        }
        composable(
            route = ClientScreen.Search.route,
            enterTransition =   { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(700)) },
            exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(700)) },
            popEnterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(700)) },
            popExitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(700)) }
        ) {
            SearchScreen(
                modifier = modifier,
                onGoBack = {
                    navController.navigate(ClientScreen.Home.route){
                        popUpTo(ClientScreen.Home.route){
                            inclusive = true
                        }
                    }
                },
                viewModel = menuScreenViewModel
            )
        }
    }
}

@Serializable
sealed class ClientScreen(val route: String){
    data object Home: ClientScreen(route = "HOME")
    data object Cart: ClientScreen(route = "CART")
    data object Orders: ClientScreen(route = "ORDERS")
    data object CreateOrder: ClientScreen(route = "CREATE_ORDER")
    data object CreateReservation: ClientScreen(route = "MAKE_RESERVATION")
    @Serializable
    data class OrderDetails(val id: Int): ClientScreen(route = "ORDER_DETAILS")
    data object Account: ClientScreen(route = "ACCOUNT")
    data object Reservations: ClientScreen(route = "RESERVATIONS")
    @Serializable
    data class ReservationDetails(val id: Int): ClientScreen(route = "RESERVATION_DETAILS")
    data object Profile: ClientScreen(route = "PROFILE")
    data object Favorites: AdminScreen(route = "FAVORITES")
    data object Search: ClientScreen(route = "SEARCH")
}