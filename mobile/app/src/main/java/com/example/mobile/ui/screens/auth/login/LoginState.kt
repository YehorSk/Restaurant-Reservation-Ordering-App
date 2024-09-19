package com.example.mobile.ui.screens.auth.login

import kotlinx.serialization.Serializable
import java.lang.Error

data class LoginState(
    val loginForm: LoginForm = LoginForm(),
    val loginFormErrors: LoginFormErrors = LoginFormErrors(),
    val isEntryValid: Boolean = false,
    val isLoggedIn: Boolean = false,
    val isLoading: Boolean = false,
    val internetError: Boolean = false,
    val error: String = "",
    val role: String = ""
)

@Serializable
data class LoginForm(
    val email: String = "",
    val password: String = ""
)

data class LoginFormErrors(
    val email: String = "",
    val password: String = ""
)