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
        startDestination = AuthScreen.Login.route
    ){

        composable(AuthScreen.SignUp.route){
            RegisterScreen(
                onLogClick = {
                    navController.navigate(AuthScreen.Login.route)
                },
                onSuccess = {
                    navController.navigate(Graph.HOME) {
                        popUpTo(AuthScreen.SignUp.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(AuthScreen.Login.route) {
            LoginScreen(
                loginViewModel = loginViewModel,
                onRegClick = {
                    navController.navigate(AuthScreen.SignUp.route)
                },
                onForgotPwdClick = {
                    navController.navigate(AuthScreen.ForgotPwd.route)
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

        composable(AuthScreen.ForgotPwd.route){
            ForgotPasswordScreen(
            )
        }
    }
}


sealed class AuthScreen(val route: String){
    object Login: AuthScreen(route = "LOGIN")
    object SignUp: AuthScreen(route = "SIGN_UP")
    object ForgotPwd: AuthScreen(route = "FORGOT_PWD")
}