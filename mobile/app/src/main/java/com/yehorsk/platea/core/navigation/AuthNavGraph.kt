package com.yehorsk.platea.core.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.yehorsk.platea.auth.presentation.forgot.ForgotPasswordScreen
import com.yehorsk.platea.auth.presentation.login.LoginScreen
import com.yehorsk.platea.auth.presentation.login.LoginViewModel
import com.yehorsk.platea.auth.presentation.register.RegisterScreen

fun NavGraphBuilder.authNavGraph(
    navController: NavController,
    loginViewModel: LoginViewModel
){
    navigation<Graph.Authentication>(
        startDestination = Screen.Login
    ){
        composable<Screen.SignUp>{
            RegisterScreen(
                onLogClick = {
                    navController.navigate(Screen.Login)
                },
                onSuccess = {
                    navController.navigate(Graph.Client) {
                        popUpTo(Screen.SignUp) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable<Screen.Login> {
            LoginScreen(
                loginViewModel = loginViewModel,
                onRegClick = {
                    navController.navigate(Screen.SignUp)
                },
                onForgotPwdClick = {
                    navController.navigate(Screen.ForgotPwd)
                },
            )
        }

        composable<Screen.ForgotPwd>{
            ForgotPasswordScreen()
        }
    }
}