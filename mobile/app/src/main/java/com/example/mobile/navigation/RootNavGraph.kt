package com.example.mobile.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mobile.ui.screens.admin.graph.AdminScreen
import com.example.mobile.ui.screens.client.graph.ClientScreenGraph
import com.example.mobile.ui.screens.waiter.graph.WaiterScreen

@Composable
fun MainNavigation(
    navController: NavHostController
) {

    NavHost(
        navController = navController,
        route = Graph.ROOT,
        startDestination = Graph.AUTHENTICATION
    ) {

        authNavGraph(
            navController = navController
        )

        composable(route = Graph.HOME) {
            ClientScreenGraph(
                onLoggedOut = {
                    navController.navigate(Graph.AUTHENTICATION) {
                        popUpTo(Graph.ROOT) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(route = Graph.WAITER) {
            WaiterScreen(
                onLoggedOut = {
                    navController.navigate(Graph.AUTHENTICATION) {
                        popUpTo(Graph.ROOT) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(route = Graph.ADMIN) {
            AdminScreen(
                onLoggedOut = {
                    navController.navigate(Graph.AUTHENTICATION) {
                        popUpTo(Graph.ROOT) {
                            inclusive = true
                        }
                    }
                }
            )
        }

    }
}

object Graph{
    const val ROOT = "root_graph"
    const val AUTHENTICATION = "auth_graph"
    const val HOME = "home_graph"
    const val WAITER = "waiter_graph"
    const val ADMIN = "admin_graph"
    const val CHEF = "chef_graph"
}
