package com.yehorsk.platea.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.yehorsk.platea.auth.presentation.login.LoginViewModel
import com.yehorsk.platea.core.data.remote.UserRoles
import com.yehorsk.platea.core.presentation.MainScreenGraph

@Composable
fun RootNavGraph(
    navController: NavHostController,
    loginViewModel: LoginViewModel,
    startDestination: String
) {
    NavHost(
        navController = navController,
        route = Graph.ROOT,
        startDestination = startDestination
    ) {

        authNavGraph(
            navController = navController,
            loginViewModel = loginViewModel
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

        composable(route = Graph.CHEF) {
            MainScreenGraph(
                onLoggedOut = {
                    navController.navigate(Graph.AUTHENTICATION) {
                        popUpTo(Graph.ROOT) {
                            inclusive = true
                        }
                    }
                },
                userRoles = UserRoles.CHEF
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
