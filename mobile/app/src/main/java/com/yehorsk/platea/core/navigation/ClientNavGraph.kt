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
import com.yehorsk.platea.core.navigation.navTypes.OrderFormNavType
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
import com.yehorsk.platea.orders.presentation.OrderForm
import com.yehorsk.platea.orders.presentation.create_order.CreateOrderScreenRoot
import com.yehorsk.platea.orders.presentation.order_details.OrderDetailsScreenRoot
import com.yehorsk.platea.orders.presentation.orders.OrdersScreen
import com.yehorsk.platea.reservations.presentation.confirm_reservation.ConfirmReservationScreenRoot
import com.yehorsk.platea.reservations.presentation.create_reservation.CreateReservationScreenRoot
import com.yehorsk.platea.reservations.presentation.create_reservation.CreateReservationViewModel
import com.yehorsk.platea.reservations.presentation.reservation_details.ReservationDetailsScreenRoot
import com.yehorsk.platea.reservations.presentation.reservations.ReservationScreenRoot
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

@Composable
fun ClientNavGraph(
    navController: NavHostController,
    modifier: Modifier,
    onLoggedOut: () -> Unit
){
    val menuScreenViewModel: MenuScreenViewModel = hiltViewModel()
    val createReservationViewModel: CreateReservationViewModel = hiltViewModel()
    val cartViewModel: CartScreenViewModel = hiltViewModel()
    val settingsViewModel: SettingsViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        route = Graph.HOME,
        startDestination = ClientScreen.Home.route
    ){
        composable(
            route = ClientScreen.Home.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            MenuScreenRoot(
                modifier = modifier,
                onSearchClicked = {
                    navController.navigate(ClientScreen.Search.route)
                },
                onCreateReservationClicked = {
                    navController.navigate(ClientScreen.CreateReservation(false))
                },
                viewModel = menuScreenViewModel,
                isUser = true
            )
        }
        composable(
            route = ClientScreen.Account.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            MainSettingsScreen(
                modifier = modifier.fillMaxSize(),
                viewModel = settingsViewModel,
                onNavigate = { destination ->
                    when(destination){
                        ProfileDestination.Favorites -> navController.navigate(ClientScreen.Favorites.route)
                        ProfileDestination.Logout -> onLoggedOut()
                        ProfileDestination.Profile -> navController.navigate(ClientScreen.Profile.route)
                        ProfileDestination.Orders -> navController.navigate(ClientScreen.Orders.route)
                        ProfileDestination.Reservations -> navController.navigate(ClientScreen.Reservations.route)
                        ProfileDestination.Language -> navController.navigate(ClientScreen.Language.route)
                        ProfileDestination.Theme -> navController.navigate(ClientScreen.Theme.route)
                        ProfileDestination.Info -> navController.navigate(ClientScreen.Info.route)
                    }
                }
            )
        }
        composable(
            route = ClientScreen.Profile.route,
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
            route = ClientScreen.Orders.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
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
        composable<ClientScreen.CreateReservation>(
            typeMap = mapOf(
                typeOf<OrderForm>() to OrderFormNavType.OrderFormType
            ),
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            val args = it.toRoute<ClientScreen.CreateReservation>()
            CreateReservationScreenRoot(
                modifier = modifier,
                viewModel = createReservationViewModel,
                goBack = {
                    navController.popBackStack()
                },
                goToFinishReservation = {
                    navController.navigate(ClientScreen.ConfirmReservation.route)
                },
                orderForm = args.orderForm,
                withOrder = args.withOrder
            )
        }
        composable(
            route = ClientScreen.ConfirmReservation.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            ConfirmReservationScreenRoot(
                modifier = modifier,
                viewModel = createReservationViewModel,
                goBack = {
                    navController.popBackStack()
                },
                goBackToMenu = {
                    navController.navigate(ClientScreen.Home.route){
                        popUpTo(ClientScreen.Home.route){
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable(
            route = ClientScreen.CreateOrder.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            CreateOrderScreenRoot(
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
                onGoToMakeReservation = {
                    navController.navigate(ClientScreen.CreateReservation(true, it)){}
                }
            )
        }
        composable<ClientScreen.OrderDetails>(
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ){
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
        composable<ClientScreen.ReservationDetails>(
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ){
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
        composable(
            route = ClientScreen.Theme.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            ChangeThemeScreen(
                modifier = modifier,
                onGoBack = { navController.popBackStack() }
            )
        }
        composable(
            route = ClientScreen.Language.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            ChangeLanguageScreen(
                modifier = modifier,
                onGoBack = { navController.popBackStack() }
            )
        }
        composable(
            route = ClientScreen.Info.route,
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
            route = ClientScreen.Favorites.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            FavoritesScreen(
                modifier = modifier,
                viewModel = menuScreenViewModel,
                onGoBack = { navController.popBackStack() }
            )
        }
        composable(
            route = ClientScreen.Cart.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
            ) {
            CartScreenRoot(
                modifier = modifier,
                viewModel = cartViewModel,
                onGoToCheckoutClick = { navController.navigate(ClientScreen.CreateOrder.route) }
            )
        }
        composable(
            route = ClientScreen.Reservations.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
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
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
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
    @Serializable
    data class CreateReservation(val withOrder: Boolean, val orderForm: OrderForm = OrderForm()): ClientScreen(route = "MAKE_RESERVATION")
    data object ConfirmReservation: ClientScreen(route = "CONFIRM_RESERVATION")
    @Serializable
    data class OrderDetails(val id: Int): ClientScreen(route = "ORDER_DETAILS")
    data object Account: ClientScreen(route = "ACCOUNT")
    data object Reservations: ClientScreen(route = "RESERVATIONS")
    @Serializable
    data class ReservationDetails(val id: Int): ClientScreen(route = "RESERVATION_DETAILS")
    data object Profile: ClientScreen(route = "PROFILE")
    data object Favorites: AdminScreen(route = "FAVORITES")
    data object Search: ClientScreen(route = "SEARCH")
    data object Theme: ClientScreen(route = "THEME")
    data object Language: ClientScreen(route = "LANGUAGE")
    data object Info: ClientScreen(route = "INFO")
}