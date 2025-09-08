package com.yehorsk.platea.core.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.yehorsk.platea.cart.presentation.cart.CartScreenRoot
import com.yehorsk.platea.cart.presentation.cart.CartScreenViewModel
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
import com.yehorsk.platea.orders.presentation.create_order.CreateOrderScreenRoot
import com.yehorsk.platea.orders.presentation.create_order.OrderForm
import com.yehorsk.platea.orders.presentation.order_details.OrderDetailsAction
import com.yehorsk.platea.orders.presentation.order_details.OrderDetailsScreenRoot
import com.yehorsk.platea.orders.presentation.order_details.OrderDetailsViewModel
import com.yehorsk.platea.orders.presentation.orders.OrdersScreen
import com.yehorsk.platea.reservations.presentation.create_reservation.ConfirmReservationScreenRoot
import com.yehorsk.platea.reservations.presentation.create_reservation.CreateReservationScreenRoot
import com.yehorsk.platea.reservations.presentation.create_reservation.CreateReservationViewModel
import com.yehorsk.platea.reservations.presentation.reservation_details.ReservationDetailsScreenRoot
import com.yehorsk.platea.reservations.presentation.reservations.ReservationScreenRoot
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
        startDestination = Screen.Home.route
    ){
        composable(
            route = Screen.Home.route
        ) {
            MenuScreenRoot(
                modifier = modifier,
                onSearchClicked = {
                    navController.navigate(Screen.Search.route)
                },
                onCreateReservationClicked = {
                    navController.navigate(Screen.CreateReservation(false))
                },
                viewModel = menuScreenViewModel,
                isUser = true
            )
        }
        composable(
            route = Screen.Account.route
        ) {
            MainSettingsScreen(
                modifier = modifier.fillMaxSize(),
                viewModel = settingsViewModel,
                onNavigate = { destination ->
                    when(destination){
                        ProfileDestination.Favorites -> navController.navigate(Screen.Favorites.route)
                        ProfileDestination.Logout -> onLoggedOut()
                        ProfileDestination.Profile -> navController.navigate(Screen.Profile.route)
                        ProfileDestination.Orders -> navController.navigate(Screen.Orders.route)
                        ProfileDestination.Reservations -> navController.navigate(Screen.Reservations.route)
                        ProfileDestination.Language -> navController.navigate(Screen.Language.route)
                        ProfileDestination.Theme -> navController.navigate(Screen.Theme.route)
                        ProfileDestination.Info -> navController.navigate(Screen.Info.route)
                    }
                }
            )
        }
        composable(
            route = Screen.Profile.route
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
            route = Screen.Orders.route
        ) {
            OrdersScreen(
                modifier = modifier,
                onGoToOrderDetails = { id ->
                    navController.navigate(Screen.OrderDetails(id))
                },
                onGoBack = {
                    navController.popBackStack()
                },
                showGoBack = true
            )
        }
        composable<Screen.CreateReservation>(
            typeMap = mapOf(
                typeOf<OrderForm>() to OrderFormNavType.OrderFormType
            )
        ) {
            val args = it.toRoute<Screen.CreateReservation>()
            CreateReservationScreenRoot(
                modifier = modifier,
                viewModel = createReservationViewModel,
                goBack = {
                    navController.popBackStack()
                },
                goToFinishReservation = {
                    navController.navigate(Screen.ConfirmReservation.route)
                },
                orderForm = args.orderForm,
                withOrder = args.withOrder
            )
        }
        composable(
            route = Screen.ConfirmReservation.route
        ) {
            ConfirmReservationScreenRoot(
                modifier = modifier,
                viewModel = createReservationViewModel,
                goBack = {
                    navController.popBackStack()
                },
                goBackToMenu = {
                    navController.navigate(Screen.Home.route){
                        popUpTo(Screen.Home.route){
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable(
            route = Screen.CreateOrder.route
        ) {
            CreateOrderScreenRoot(
                modifier = modifier,
                onGoToCart = {
                    navController.navigate(Screen.Cart.route){
                        popUpTo(Screen.Cart.route){
                            inclusive = true
                        }
                    }
                },
                onGoToMenu = {
                    navController.navigate(Screen.Home.route){
                        popUpTo(Screen.Home.route){
                            inclusive = true
                        }
                    }
                },
                onGoToMakeReservation = {
                    navController.navigate(Screen.CreateReservation(true, it)){}
                }
            )
        }
        composable<Screen.OrderDetails>(){
            val args = it.toRoute<Screen.OrderDetails>()
            val viewModel: OrderDetailsViewModel = hiltViewModel()
            LaunchedEffect(args.id) {
                args.id.let { id ->
                    viewModel.onAction(OrderDetailsAction.OnGetOrderById(id.toString()))
                }
            }
            OrderDetailsScreenRoot(
                viewModel = viewModel,
                modifier = modifier,
                onGoBack = {
                    navController.navigate(Screen.Orders.route){
                        popUpTo(Screen.Orders.route){
                            inclusive = true
                        }
                    }
                },
                id = args.id,
                onOpenItemDetails = {}
            )
        }
        composable<Screen.ReservationDetails>(){
            val args = it.toRoute<Screen.ReservationDetails>()
            ReservationDetailsScreenRoot(
                modifier = modifier,
                onGoBack = {
                    navController.navigate(Screen.Reservations.route){
                        popUpTo(Screen.Reservations.route){
                            inclusive = true
                        }
                    }
                },
                id = args.id
            )
        }
        composable(
            route = Screen.Theme.route
        ) {
            ChangeThemeScreen(
                modifier = modifier,
                onGoBack = { navController.popBackStack() }
            )
        }
        composable(
            route = Screen.Language.route
        ) {
            ChangeLanguageScreen(
                modifier = modifier,
                onGoBack = { navController.popBackStack() }
            )
        }
        composable(
            route = Screen.Info.route
        ) {
            RestaurantInfoScreenRoot(
                modifier = modifier,
                viewModel = settingsViewModel,
                onGoBack = { navController.popBackStack() }
            )
        }
        composable(
            route = Screen.Favorites.route
        ) {
            FavoritesScreen(
                modifier = modifier,
                viewModel = menuScreenViewModel,
                onGoBack = { navController.popBackStack() }
            )
        }
        composable(
            route = Screen.Cart.route
            ) {
            CartScreenRoot(
                modifier = modifier,
                viewModel = cartViewModel,
                onGoToCheckoutClick = { navController.navigate(Screen.CreateOrder.route) }
            )
        }
        composable(
            route = Screen.Reservations.route
        ) {
            ReservationScreenRoot(
                modifier = modifier,
                onGoToReservationDetails = {id ->
                    navController.navigate(Screen.ReservationDetails(id))},
                onGoBack = { navController.popBackStack() },
                showGoBack = true
            )
        }
        composable(
            route = Screen.Search.route
        ) {
            SearchScreen(
                modifier = modifier,
                onGoBack = {
                    navController.navigate(Screen.Home.route){
                        popUpTo(Screen.Home.route){
                            inclusive = true
                        }
                    }
                },
                viewModel = menuScreenViewModel
            )
        }
    }
}