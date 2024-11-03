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
import com.example.mobile.ui.screens.waiter.home.WaiterMainScreen

@Composable
fun WaiterNavGraph(
    modifier:Modifier = Modifier,
    navController: NavHostController,
    onLoggedOut: () -> Unit
){
    Scaffold { padding ->
        NavHost(
            navController = navController,
            route = Graph.WAITER,
            startDestination = WaiterScreen.Home.route
        ){
            composable(WaiterScreen.Home.route) {
                WaiterMainScreen(
                    modifier = Modifier.padding(padding)
                )
            }
            composable(WaiterScreen.Settings.route) {
                SettingsScreen(
                    modifier = modifier.fillMaxSize().padding(padding),
                    onSuccessLoggedOut = onLoggedOut
                )
            }
        }
    }
}

sealed class WaiterScreen(val route: String){
    object Home: WaiterScreen(route = "HOME")
    object Settings: WaiterScreen(route = "SETTINGS")
}