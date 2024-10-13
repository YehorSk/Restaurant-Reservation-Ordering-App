package com.example.mobile.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mobile.cart.presentation.cart.CartScreen
import com.example.mobile.menu.presentation.menu.MenuScreen
import com.example.mobile.orders.presentation.orders.OrdersScreen
import com.example.mobile.core.presentation.settings.SettingsScreen
import com.example.mobile.favorites.presentation.favorites.FavoritesScreen

@Composable
fun HomeNavGraph(
    navController: NavHostController,
    modifier: Modifier,
    onLoggedOut: () -> Unit
){
    NavHost(
        navController = navController,
        route = Graph.HOME,
        startDestination = HomeScreen.Home.route
    ){
        composable(HomeScreen.Home.route) {
            MenuScreen(
                modifier = modifier
            )
        }
        composable(HomeScreen.Settings.route) {
            SettingsScreen(
                modifier = modifier.fillMaxSize(),
                onSuccessLoggedOut = onLoggedOut
            )
        }
        composable(HomeScreen.Orders.route) {
            OrdersScreen(
                modifier = modifier
            )
        }
        composable(HomeScreen.Favorites.route) {
            FavoritesScreen(
                modifier = modifier
            )
        }
        composable(HomeScreen.Cart.route) {
            CartScreen(
                modifier = modifier
            )
        }
    }
}


sealed class HomeScreen(val route: String){
    object Home: HomeScreen(route = "HOME")
    object Cart: HomeScreen(route = "CART")
    object Orders: HomeScreen(route = "ORDERS")
    object Settings: HomeScreen(route = "SETTINGS")
    object Favorites: HomeScreen(route = "FAVORITES")
}