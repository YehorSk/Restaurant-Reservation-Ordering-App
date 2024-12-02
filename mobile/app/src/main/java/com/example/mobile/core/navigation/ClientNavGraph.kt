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
import com.example.mobile.cart.presentation.cart.CartScreen
import com.example.mobile.cart.presentation.cart.viewmodel.CartScreenViewModel
import com.example.mobile.menu.presentation.menu.MenuScreen
import com.example.mobile.orders.presentation.orders.OrdersScreen
import com.example.mobile.core.presentation.settings.SettingsScreen
import com.example.mobile.menu.presentation.favorites.FavoritesScreen
import com.example.mobile.menu.presentation.menu.viewmodel.MenuScreenViewModel
import com.example.mobile.menu.presentation.search.SearchScreen
import com.example.mobile.orders.presentation.create_order.CreateOrderScreen
import com.example.mobile.orders.presentation.order_details.OrderDetailsScreen
import com.example.mobile.orders.presentation.orders.viewmodel.OrdersViewModel

@Composable
fun ClientNavGraph(
    navController: NavHostController,
    modifier: Modifier,
    onLoggedOut: () -> Unit
){
    val menuScreenViewModel: MenuScreenViewModel = hiltViewModel()
    val cartScreenViewModel: CartScreenViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        route = Graph.HOME,
        startDestination = ClientScreen.Home.route
    ){
        composable(
            route = ClientScreen.Home.route,
        ) {
            MenuScreen(
                modifier = modifier,
                onSearchClicked = {
                    navController.navigate(ClientScreen.Search.route)
                },
                viewModel = menuScreenViewModel
            )
        }
        composable(ClientScreen.Settings.route) {
            SettingsScreen(
                modifier = modifier.fillMaxSize(),
                onSuccessLoggedOut = onLoggedOut
            )
        }
        composable(ClientScreen.Orders.route) {
            OrdersScreen(
                modifier = modifier,
                onGoToOrderDetails = {
                    navController.navigate(ClientScreen.OrderDetails.route)
                }
            )
        }
        composable(ClientScreen.CreateOrder.route) {
            CreateOrderScreen(
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
                }
            )
        }
        composable(ClientScreen.OrderDetails.route){
            OrderDetailsScreen(

            )
        }
        composable(ClientScreen.Favorites.route) {
            FavoritesScreen(
                modifier = modifier,
                viewModel = menuScreenViewModel
            )
        }
        composable(
            route = ClientScreen.Cart.route,
//            enterTransition =   { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(700)) },
//            exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(700)) },
//            popEnterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(700)) },
//            popExitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(700)) }
        ) {
            CartScreen(
                modifier = modifier,
                viewModel = cartScreenViewModel,
                onGoToCheckoutClick = { navController.navigate(ClientScreen.CreateOrder.route) }
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


sealed class ClientScreen(val route: String){
    data object Home: ClientScreen(route = "HOME")
    data object Cart: ClientScreen(route = "CART")
    data object Orders: ClientScreen(route = "ORDERS")
    data object CreateOrder: ClientScreen(route = "CREATE_ORDER")
    data object OrderDetails: ClientScreen(route = "ORDER_DETAILS")
    data object Settings: ClientScreen(route = "SETTINGS")
    data object Favorites: ClientScreen(route = "FAVORITES")
    data object Search: ClientScreen(route = "SEARCH")
}