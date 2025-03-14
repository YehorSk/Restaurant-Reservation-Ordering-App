package com.yehorsk.platea.auth.presentation.register

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class RegisterState(
    val registerForm: RegisterForm = RegisterForm(),
    val registerFormErrors: RegisterFormErrors = RegisterFormErrors(),
    val isEntryValid: Boolean = false,
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false
)

@Serializable
data class RegisterForm(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    @SerialName("password_confirmation")
    val passwordConfirm: String = "",
    val message: String = "",
    @SerialName("fcm_token")
    val fcmToken: String = "",
    @SerialName("device_id")
    val deviceId: String = "",
    @SerialName("device_type")
    val deviceType: String = "",
)

data class RegisterFormErrors(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val passwordConfirm: String = "",
    val message: String = ""
)