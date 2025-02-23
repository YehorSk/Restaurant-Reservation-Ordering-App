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
import com.yehorsk.platea.core.presentation.settings.SettingsViewModel
import com.yehorsk.platea.menu.presentation.favorites.FavoritesScreen
import com.yehorsk.platea.menu.presentation.menu.MenuScreenViewModel
import com.yehorsk.platea.orders.presentation.order_details.OrderDetailsScreenRoot
import com.yehorsk.platea.orders.presentation.order_details.OrderItemDetailsScreenRoot
import com.yehorsk.platea.orders.presentation.orders.OrdersScreen
import kotlinx.serialization.Serializable

@Composable
fun ChefNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    onLoggedOut: () -> Unit
){
    val menuScreenViewModel: MenuScreenViewModel = hiltViewModel()
    val settingsViewModel: SettingsViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        route = Graph.CHEF,
        startDestination = ChefScreen.Orders.route
    ){
        composable(
            route = ChefScreen.Account.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            MainSettingsScreen(
                modifier = modifier.fillMaxSize(),
                viewModel = settingsViewModel,
                onNavigate = { destination ->
                    when(destination){
                        ProfileDestination.Favorites -> navController.navigate(ChefScreen.Favorites.route)
                        ProfileDestination.Logout -> onLoggedOut()
                        ProfileDestination.Profile -> navController.navigate(ChefScreen.Profile.route)
                        ProfileDestination.Orders -> {}
                        ProfileDestination.Reservations -> {}
                        ProfileDestination.Language -> navController.navigate(ChefScreen.Language.route)
                        ProfileDestination.Theme -> navController.navigate(ChefScreen.Theme.route)
                    }
                }
            )
        }
        composable(
            route = ChefScreen.Theme.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            ChangeThemeScreen(
                modifier = modifier,
                onGoBack = { navController.popBackStack() }
            )
        }
        composable(
            route = ChefScreen.Language.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            ChangeLanguageScreen(
                modifier = modifier,
                onGoBack = { navController.popBackStack() }
            )
        }
        composable(
            route = ChefScreen.Profile.route,
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
            route = ChefScreen.Orders.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            OrdersScreen(
                modifier = modifier,
                onGoToOrderDetails = { id ->
                    navController.navigate(ChefScreen.OrderDetails(id))
                },
                onGoBack = {
                    navController.popBackStack()
                },
                showGoBack = false,
                title = R.string.orders
            )
        }
        composable(
            route = ChefScreen.Favorites.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            FavoritesScreen(
                modifier = modifier,
                viewModel = menuScreenViewModel,
                onGoBack = { navController.popBackStack() }
            )
        }
        composable<ChefScreen.OrderDetails>(
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ){
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
        composable<ChefScreen.OrderItemDetails>(
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ){
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
    object Account: ChefScreen(route = "ACCOUNT")
    data object Profile: ChefScreen(route = "PROFILE")
    object Orders: ChefScreen(route = "ORDERS")
    data object Favorites: ChefScreen(route = "FAVORITES")
    @Serializable
    data class OrderDetails(val id: Int): ChefScreen(route = "ORDER_DETAILS")
    @Serializable
    data class OrderItemDetails(val id: Int): ChefScreen(route = "ORDER_ITEM_DETAILS")
    data object Theme: AdminScreen(route = "THEME")
    data object Language: AdminScreen(route = "LANGUAGE")
}