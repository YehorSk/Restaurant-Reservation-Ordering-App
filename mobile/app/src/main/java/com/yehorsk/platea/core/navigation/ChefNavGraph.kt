package com.yehorsk.platea.core.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.yehorsk.platea.R
import com.yehorsk.platea.core.presentation.settings.ChangeLanguageScreen
import com.yehorsk.platea.core.presentation.settings.ChangeThemeScreen
import com.yehorsk.platea.core.presentation.settings.MainSettingsScreen
import com.yehorsk.platea.core.presentation.settings.ProfileDestination
import com.yehorsk.platea.core.presentation.settings.ProfileScreen
import com.yehorsk.platea.core.presentation.settings.RestaurantInfoScreenRoot
import com.yehorsk.platea.core.presentation.settings.SettingsViewModel
import com.yehorsk.platea.menu.presentation.menu.FavoritesScreen
import com.yehorsk.platea.menu.presentation.menu.MenuScreenViewModel
import com.yehorsk.platea.orders.presentation.order_details.OrderDetailsAction
import com.yehorsk.platea.orders.presentation.order_details.OrderDetailsScreenRoot
import com.yehorsk.platea.orders.presentation.order_details.OrderDetailsViewModel
import com.yehorsk.platea.orders.presentation.order_details.OrderItemDetailsScreenRoot
import com.yehorsk.platea.orders.presentation.orders.OrdersScreen

fun NavGraphBuilder.chefNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    onLoggedOut: () -> Unit
){
    navigation<Graph.Chef>(
        startDestination = Screen.Orders
    ){
        composable<Screen.Account>{
            val settingsViewModel = it.sharedHiltViewModel<SettingsViewModel>(navController)

            MainSettingsScreen(
                modifier = modifier.fillMaxSize(),
                viewModel = settingsViewModel,
                onNavigate = { destination ->
                    when(destination){
                        ProfileDestination.Favorites -> navController.navigate(Screen.Favorites)
                        ProfileDestination.Logout -> onLoggedOut()
                        ProfileDestination.Profile -> navController.navigate(Screen.Profile)
                        ProfileDestination.Orders -> {}
                        ProfileDestination.Reservations -> {}
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
                showGoBack = false,
                title = R.string.orders,
            )
        }
        composable<Screen.Favorites>{
            val menuScreenViewModel: MenuScreenViewModel = hiltViewModel()

            FavoritesScreen(
                modifier = modifier,
                viewModel = menuScreenViewModel,
                onGoBack = { navController.popBackStack() }
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
                onOpenItemDetails = { id ->
                    navController.navigate(Screen.OrderItemDetails(id))
                }
            )
        }
        composable<Screen.OrderItemDetails>{
            val args = it.toRoute<Screen.OrderItemDetails>()
            val viewModel: OrderDetailsViewModel = hiltViewModel()
            LaunchedEffect(args.id) {
                args.id.let { id ->
                    viewModel.onAction(OrderDetailsAction.OnGetOrderById(id.toString()))
                }
            }
            OrderItemDetailsScreenRoot(
                viewModel = viewModel,
                modifier = modifier,
                onGoBack = {
                    navController.popBackStack()
                },
                id = args.id
            )
        }
    }
}