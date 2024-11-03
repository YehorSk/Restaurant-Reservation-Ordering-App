package com.example.mobile.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mobile.core.presentation.settings.SettingsScreen
import com.example.mobile.ui.screens.admin.home.AdminMainScreen
import com.example.mobile.ui.screens.waiter.home.WaiterMainScreen

@Composable
fun AdminNavGraph(
    modifier:Modifier = Modifier,
    navController: NavHostController,
    onLoggedOut: () -> Unit
){
    Scaffold { padding ->
        NavHost(
            navController = navController,
            route = Graph.WAITER,
            startDestination = AdminScreen.Home.route
        ){
            composable(AdminScreen.Home.route) {
                AdminMainScreen(
                    modifier = Modifier.padding(padding)
                )
            }
            composable(AdminScreen.Settings.route) {
                SettingsScreen(
                    modifier = modifier.fillMaxSize().padding(padding),
                    onSuccessLoggedOut = onLoggedOut
                )
            }
        }
    }
}

sealed class AdminScreen(val route: String){
    object Home: AdminScreen(route = "HOME")
    object Settings: AdminScreen(route = "SETTINGS")
}