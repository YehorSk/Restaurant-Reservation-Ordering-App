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
import com.example.mobile.cart.presentation.cart.viewmodel.CartScreenViewModel
import com.example.mobile.core.presentation.settings.ProfileDestination
import com.example.mobile.core.presentation.settings.ProfileScreen
import com.example.mobile.menu.presentation.favorites.FavoritesScreen
import com.example.mobile.menu.presentation.menu.MenuScreenRoot
import com.example.mobile.menu.presentation.menu.viewmodel.MenuScreenViewModel
import com.example.mobile.menu.presentation.search.SearchScreen
import com.example.mobile.orders.presentation.order_details.OrderDetailsScreenRoot
import com.example.mobile.orders.presentation.order_details.OrderItemDetailsScreenRoot
import com.example.mobile.orders.presentation.orders.OrdersScreen
import kotlinx.serialization.Serializable

@Composable
fun ChefNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    onLoggedOut: () -> Unit
){
    val menuScreenViewModel: MenuScreenViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        route = Graph.CHEF,
        startDestination = ChefScreen.Orders.route
    ){
        composable(ChefScreen.Profile.route) {
            ProfileScreen(
                modifier = modifier.fillMaxSize(),
                onNavigate = { destination ->
                    when(destination){
                        ProfileDestination.Favorites -> navController.navigate(ChefScreen.Favorites.route)
                        ProfileDestination.Logout -> onLoggedOut()
                        ProfileDestination.Settings -> {}
                    }
                }
            )
        }
        composable(ChefScreen.Settings.route) {
            ProfileScreen(
                modifier = modifier.fillMaxSize(),
                onNavigate = { destination ->
                    when(destination){
                        ProfileDestination.Favorites -> navController.navigate(ChefScreen.Favorites.route)
                        ProfileDestination.Logout -> onLoggedOut()
                        ProfileDestination.Settings -> {}
                    }
                }
            )
        }
        composable(ChefScreen.Orders.route) {
            OrdersScreen(
                modifier = modifier,
                onGoToOrderDetails = { id ->
                    navController.navigate(ChefScreen.OrderDetails(id))
                }
            )
        }
        composable(ChefScreen.Favorites.route) {
            FavoritesScreen(
                modifier = modifier,
                viewModel = menuScreenViewModel,
                onGoBack = { navController.popBackStack() }
            )
        }
        composable<ChefScreen.OrderDetails>{
            val args = it.toRoute<ChefScreen.OrderDetails>()
            OrderDetailsScreenRoot(
                modifier = modifier,
                onGoBack = {
                    navController.navigate(ChefScreen.Orders.route){
                        popUpTo(ChefScreen.Orders.route){
                            inclusive = true
                        }
                    }
                },
                id = args.id,
                onOpenItemDetails = { id ->
                    navController.navigate(ChefScreen.OrderItemDetails(id))
                }
            )
        }
        composable<ChefScreen.OrderItemDetails>{
            val args = it.toRoute<ChefScreen.OrderItemDetails>()
            OrderItemDetailsScreenRoot(
                modifier = modifier,
                onGoBack = {
                    navController.popBackStack()
                },
                id = args.id
            )
        }
    }
}

@Serializable
sealed class ChefScreen(val route: String){
    object Settings: ChefScreen(route = "SETTINGS")
    data object Profile: ChefScreen(route = "PROFILE")
    object Orders: ChefScreen(route = "ORDERS")
    data object Favorites: ChefScreen(route = "FAVORITES")
    @Serializable
    data class OrderDetails(val id: Int): ChefScreen(route = "ORDER_DETAILS")
    @Serializable
    data class OrderItemDetails(val id: Int): ChefScreen(route = "ORDER_ITEM_DETAILS")
}