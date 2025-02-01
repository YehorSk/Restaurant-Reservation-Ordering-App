package com.yehorsk.platea.auth.presentation.login

import kotlinx.serialization.SerialName
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
    val password: String = "",
    @SerialName("fcm_token")
    val fcmToken: String = "",
    @SerialName("device_id")
    val deviceId: String = "",
    @SerialName("device_type")
    val deviceType: String = "",
)

@Serializable
data class AuthState(
    @SerialName("fcm_token")
    val fcmToken: String = "",
    @SerialName("device_id")
    val deviceId: String = "",
    @SerialName("device_type")
    val deviceType: String = "",
)
