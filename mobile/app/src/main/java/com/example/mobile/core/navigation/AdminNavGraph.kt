package com.example.mobile.core.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mobile.core.presentation.settings.SettingsScreen

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
                Box(
                    modifier = Modifier.padding(padding)
                ){
                    Text(text = "Admin page")
                }
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