package com.example.mobile.core.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mobile.core.presentation.settings.ProfileDestination
import com.example.mobile.core.presentation.settings.ProfileScreen
import com.example.mobile.core.presentation.settings.SettingsScreen
import com.example.mobile.menu.presentation.menu_admin.MenuAdminScreen
import com.example.mobile.menu.presentation.menu_admin.viewmodel.MenuAdminScreenViewModel

@Composable
fun AdminNavGraph(
    modifier:Modifier = Modifier,
    navController: NavHostController,
    onLoggedOut: () -> Unit
){

    val menuScreenViewModel: MenuAdminScreenViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        route = Graph.WAITER,
        startDestination = AdminScreen.Home.route
    ){
        composable(AdminScreen.Home.route) {
            MenuAdminScreen(
                modifier = modifier,
                onSearchClicked = {
                    navController.navigate(AdminScreen.Search.route)
                },
                viewModel = menuScreenViewModel
            )
        }
        composable(AdminScreen.Settings.route) {
            SettingsScreen(
                modifier = modifier.fillMaxSize(),
                onNavigate = { destination ->
                    when(destination){
                        ProfileDestination.Favorites -> navController.navigate(ClientScreen.Favorites.route)
                        ProfileDestination.Logout -> onLoggedOut()
                        ProfileDestination.Profile -> navController.navigate(ClientScreen.Profile.route)
                    }
                }
            )
        }
        composable(AdminScreen.Profile.route) {
            ProfileScreen(
                modifier = modifier,
                onGoBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}

sealed class AdminScreen(val route: String){
    object Home: AdminScreen(route = "HOME")
    data object Profile: AdminScreen(route = "PROFILE")
    object Settings: AdminScreen(route = "SETTINGS")
    data object Search: AdminScreen(route = "SEARCH")
}