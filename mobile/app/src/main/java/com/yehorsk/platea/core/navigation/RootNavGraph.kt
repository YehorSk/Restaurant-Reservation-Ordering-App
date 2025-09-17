package com.yehorsk.platea.core.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.yehorsk.platea.auth.presentation.login.LoginViewModel
import com.yehorsk.platea.core.data.remote.UserRoles
import com.yehorsk.platea.core.presentation.MainScreenViewModel
import com.yehorsk.platea.core.presentation.components.BottomBar
import com.yehorsk.platea.core.utils.snackbar.LocalSnackbarHostState
import kotlinx.serialization.Serializable

@Composable
fun RootNavGraph(
    navController: NavHostController,
    loginViewModel: LoginViewModel,
    mainScreenViewModel: MainScreenViewModel,
    startDestination: Graph,
    userRoles: UserRoles,
) {

    val uiState by mainScreenViewModel.cartItemCount.collectAsStateWithLifecycle()

    val snackbarHostState = LocalSnackbarHostState.current

    Scaffold(
        bottomBar = {
            BottomBar(
                navController = navController,
                userRoles = userRoles,
                cartItems = uiState
            )
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState
            )
        }
    ) { contentPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
        ) {
            authNavGraph(
                navController = navController,
                loginViewModel = loginViewModel
            )

            clientNavGraph(
                modifier = Modifier.padding(contentPadding),
                navController = navController,
                onLoggedOut = {
                    navController.navigate(Graph.Authentication) {
                        popUpTo(Graph.Root) {
                            inclusive = true
                        }
                    }
                }
            )

            waiterNavGraph(
                modifier = Modifier.padding(contentPadding),
                navController = navController,
                onLoggedOut = {
                    navController.navigate(Graph.Authentication) {
                        popUpTo(Graph.Root) {
                            inclusive = true
                        }
                    }
                },
            )

            adminNavGraph(
                modifier = Modifier.padding(contentPadding),
                navController = navController,
                onLoggedOut = {
                    navController.navigate(Graph.Authentication) {
                        popUpTo(Graph.Root) {
                            inclusive = true
                        }
                    }
                },
            )

            chefNavGraph(
                modifier = Modifier.padding(contentPadding),
                navController = navController,
                onLoggedOut = {
                    navController.navigate(Graph.Authentication) {
                        popUpTo(Graph.Root) {
                            inclusive = true
                        }
                    }
                },
            )
        }
    }
}
@Serializable
sealed class Graph{
    @Serializable data object Root: Graph()
    @Serializable data object Authentication: Graph()
    @Serializable data object Client: Graph()
    @Serializable data object Waiter: Graph()
    @Serializable data object Admin: Graph()
    @Serializable data object Chef: Graph()
}

@Composable
inline fun <reified T: ViewModel> NavBackStackEntry.sharedHiltViewModel(
    navController: NavController
): T{
    val navGraphRoute = destination.parent?.route ?: return hiltViewModel<T>()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return hiltViewModel(
        viewModelStoreOwner = parentEntry
    )
}