package com.yehorsk.platea.core.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.yehorsk.platea.auth.presentation.forgot.ForgotPasswordScreen
import com.yehorsk.platea.auth.presentation.login.LoginScreen
import com.yehorsk.platea.auth.presentation.login.LoginViewModel
import com.yehorsk.platea.auth.presentation.register.RegisterScreen
import timber.log.Timber

fun NavGraphBuilder.authNavGraph(
    navController: NavController,
    loginViewModel: LoginViewModel
){
    navigation(
        route = Graph.AUTHENTICATION,
        startDestination = Screen.Login.route
    ){
        composable(Screen.SignUp.route){
            RegisterScreen(
                onLogClick = {
                    navController.navigate(Screen.Login.route)
                },
                onSuccess = {
                    navController.navigate(Graph.HOME) {
                        popUpTo(Screen.SignUp.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(Screen.Login.route) {
            LoginScreen(
                loginViewModel = loginViewModel,
                onRegClick = {
                    navController.navigate(Screen.SignUp.route)
                },
                onForgotPwdClick = {
                    navController.navigate(Screen.ForgotPwd.route)
                },
                onSuccessClient = {
                    Timber.d("Navigating to client screen")
                    navController.navigate(Graph.HOME) {
                        popUpTo(Graph.ROOT) {
                            inclusive = true
                        }
                    }
                },
                onSuccessWaiter = {
                    Timber.d("Navigating to waiter screen")
                    navController.navigate(Graph.WAITER) {
                        popUpTo(Graph.ROOT) {
                            inclusive = true
                        }
                    }
                },
                onSuccessAdmin = {
                    Timber.d("Navigating to admin screen")
                    navController.navigate(Graph.ADMIN) {
                        popUpTo(Graph.ROOT) {
                            inclusive = true
                        }
                    }
                },
                onSuccessChef = {
                    Timber.d("Navigating to chef screen")
                    navController.navigate(Graph.CHEF) {
                        popUpTo(Graph.ROOT) {
                            inclusive = true
                        }
                    }
                },
            )
        }

        composable(Screen.ForgotPwd.route){
            ForgotPasswordScreen(
            )
        }
    }
}