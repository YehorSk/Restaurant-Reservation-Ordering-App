package com.example.mobile.auth.presentation.login

import kotlinx.serialization.Serializable

data class LoginState(
    val loginForm: LoginForm = LoginForm(),
    val isEntryValid: Boolean = false,
    val isLoggedIn: Boolean = false,
    val isLoading: Boolean = false,
    val error: String = "",
    val role: String = ""
)

@Serializable
data class LoginForm(
    val email: String = "",
    val password: String = ""
)
