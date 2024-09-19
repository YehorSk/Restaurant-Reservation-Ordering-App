package com.example.mobile.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mobile.ui.screens.client.home.MainScreen
import com.example.mobile.ui.screens.common.settings.SettingsScreen

@Composable
fun HomeNavGraph(
    navController: NavHostController,
    modifier: Modifier,
    onLoggedOut: () -> Unit
){
    NavHost(
        navController = navController,
        route = Graph.HOME,
        startDestination = HomeScreen.Home.route
    ){
        composable(HomeScreen.Home.route) {
            MainScreen(
                modifier = modifier
            )
        }
        composable(HomeScreen.Settings.route) {
            SettingsScreen(
                modifier = modifier.fillMaxSize(),
                onSuccessLoggedOut = onLoggedOut
            )
        }
    }
}


sealed class HomeScreen(val route: String){
    object Home: HomeScreen(route = "HOME")
    object Settings: HomeScreen(route = "SETTINGS")
}