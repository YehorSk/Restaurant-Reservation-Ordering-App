package com.example.mobile.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mobile.core.data.remote.UserRoles
import com.example.mobile.ui.screens.graph.MainScreenGraph

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
            MainScreenGraph(
                onLoggedOut = {
                    navController.navigate(Graph.AUTHENTICATION) {
                        popUpTo(Graph.ROOT) {
                            inclusive = true
                        }
                    }
                },
                userRoles = UserRoles.USER
            )
        }

        composable(route = Graph.WAITER) {
            MainScreenGraph(
                onLoggedOut = {
                    navController.navigate(Graph.AUTHENTICATION) {
                        popUpTo(Graph.ROOT) {
                            inclusive = true
                        }
                    }
                },
                userRoles = UserRoles.WAITER
            )
        }

        composable(route = Graph.ADMIN) {
            MainScreenGraph(
                onLoggedOut = {
                    navController.navigate(Graph.AUTHENTICATION) {
                        popUpTo(Graph.ROOT) {
                            inclusive = true
                        }
                    }
                },
                userRoles = UserRoles.ADMIN
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
