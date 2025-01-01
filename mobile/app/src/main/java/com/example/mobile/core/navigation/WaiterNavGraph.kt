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
import com.example.mobile.core.presentation.settings.SettingsScreen
import com.example.mobile.menu.presentation.favorites.FavoritesScreen
import com.example.mobile.menu.presentation.menu.MenuScreenRoot
import com.example.mobile.menu.presentation.menu.viewmodel.MenuScreenViewModel
import com.example.mobile.menu.presentation.search.SearchScreen
import com.example.mobile.orders.presentation.create_order.waiter.WaiterCreateOrderScreen
import com.example.mobile.orders.presentation.order_details.OrderDetailsScreenRoot
import com.example.mobile.orders.presentation.orders.OrdersScreen
import kotlinx.serialization.Serializable

@Composable
fun WaiterNavGraph(
    modifier:Modifier = Modifier,
    navController: NavHostController,
    onLoggedOut: () -> Unit
){

    val menuScreenViewModel: MenuScreenViewModel = hiltViewModel()
    val cartScreenViewModel: CartScreenViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        route = Graph.WAITER,
        startDestination = WaiterScreen.Home.route
    ){
        composable(WaiterScreen.Home.route) {
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
        composable(WaiterScreen.Settings.route) {
            SettingsScreen(
                modifier = modifier.fillMaxSize(),
                onNavigate = { destination ->
                    when(destination){
                        ProfileDestination.Favorites -> navController.navigate(WaiterScreen.Favorites.route)
                        ProfileDestination.Logout -> onLoggedOut()
                        ProfileDestination.Profile -> navController.navigate(WaiterScreen.Profile.route)
                    }
                }
            )
        }
        composable(WaiterScreen.Profile.route) {
            ProfileScreen(
                modifier = modifier,
                onGoBack = {
                    navController.popBackStack()
                }
            )
        }
        composable(WaiterScreen.Orders.route) {
            OrdersScreen(
                modifier = modifier,
                onGoToOrderDetails = { id ->
                    navController.navigate(WaiterScreen.OrderDetails(id))
                }
            )
        }
        composable(
            route = WaiterScreen.Search.route,
            enterTransition =   { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(700)) },
            exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(700)) },
            popEnterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(700)) },
            popExitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(700)) }
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
        ) {
            CartScreenRoot(
                modifier = modifier,
                viewModel = cartScreenViewModel,
                onGoToCheckoutClick = {
                    navController.navigate(WaiterScreen.CreateOrder.route)
                }
            )
        }
        composable(WaiterScreen.Favorites.route) {
            FavoritesScreen(
                modifier = modifier,
                viewModel = menuScreenViewModel,
                onGoBack = { navController.popBackStack() }
            )
        }
        composable(WaiterScreen.CreateOrder.route) {
            WaiterCreateOrderScreen(
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
                onGoToOrders = {
                    navController.navigate(WaiterScreen.Orders.route){
                        popUpTo(WaiterScreen.Orders.route){
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable<WaiterScreen.OrderDetails>{
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
    }
}

@Serializable
sealed class WaiterScreen(val route: String){
    object Home: WaiterScreen(route = "HOME")
    object Settings: WaiterScreen(route = "SETTINGS")
    data object Profile: WaiterScreen(route = "PROFILE")
    object Cart: WaiterScreen(route = "CART")
    object Orders: WaiterScreen(route = "ORDERS")
    object CreateOrder: WaiterScreen(route = "CREATE_ORDER")
    data object Favorites: WaiterScreen(route = "FAVORITES")
    data object Search: WaiterScreen(route = "SEARCH")
    @Serializable
    data class OrderDetails(val id: Int): WaiterScreen(route = "ORDER_DETAILS")
}