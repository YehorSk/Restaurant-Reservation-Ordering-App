package com.example.mobile.navigation

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
import com.example.mobile.favorites.presentation.favorites.FavoritesScreen
import com.example.mobile.menu.presentation.menu.viewmodel.MenuScreenViewModel
import com.example.mobile.menu.presentation.search.SearchScreen

@Composable
fun HomeNavGraph(
    navController: NavHostController,
    modifier: Modifier,
    onLoggedOut: () -> Unit
){
    val menuScreenViewModel: MenuScreenViewModel = hiltViewModel()
    val cartScreenViewModel: CartScreenViewModel = hiltViewModel()
    NavHost(
        navController = navController,
        route = Graph.HOME,
        startDestination = HomeScreen.Home.route
    ){
        composable(
            route = HomeScreen.Home.route,
        ) {
            MenuScreen(
                modifier = modifier,
                onSearchClicked = {
                    navController.navigate(HomeScreen.Search.route)
                },
                viewModel = menuScreenViewModel
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
                modifier = modifier,
                viewModel = cartScreenViewModel
            )
        }
        composable(
            route = HomeScreen.Search.route,
            enterTransition =   { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(700)) },
            exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(700)) },
            popEnterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(700)) },
            popExitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(700)) }
        ) {
            SearchScreen(
                modifier = modifier,
                onGoBack = {
                    navController.navigate(HomeScreen.Home.route){
                        popUpTo(HomeScreen.Home.route){
                            inclusive = true
                        }
                    }
                },
                viewModel = menuScreenViewModel
            )
        }
    }
}


sealed class HomeScreen(val route: String){
    data object Home: HomeScreen(route = "HOME")
    data object Cart: HomeScreen(route = "CART")
    data object Orders: HomeScreen(route = "ORDERS")
    data object Settings: HomeScreen(route = "SETTINGS")
    data object Favorites: HomeScreen(route = "FAVORITES")
    data object Search: HomeScreen(route = "SEARCH")
}