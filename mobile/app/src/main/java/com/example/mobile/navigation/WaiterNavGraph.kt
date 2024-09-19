package com.example.mobile.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mobile.ui.screens.common.settings.SettingsScreen
import com.example.mobile.ui.screens.waiter.home.MainScreen

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
            composable(HomeScreen.Home.route) {
                MainScreen(
                    modifier = Modifier.padding(padding)
                )
            }
            composable(HomeScreen.Settings.route) {
                SettingsScreen(
                    modifier = modifier.fillMaxSize().padding(padding),
                    onSuccessLoggedOut = onLoggedOut
                )
            }
        }
    }
}

sealed class WaiterScreen(val route: String){
    object Home: HomeScreen(route = "HOME")
    object Settings: HomeScreen(route = "SETTINGS")
}