package com.example.mobile.core.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mobile.core.presentation.settings.ProfileScreen
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
                    navController.navigate(ClientScreen.Search.route)
                },
                viewModel = menuScreenViewModel
            )
        }
        composable(AdminScreen.Settings.route) {
            ProfileScreen(
                modifier = modifier.fillMaxSize(),
                onSuccessLoggedOut = onLoggedOut
            )
        }
    }
}

sealed class AdminScreen(val route: String){
    object Home: AdminScreen(route = "HOME")
    object Settings: AdminScreen(route = "SETTINGS")
}