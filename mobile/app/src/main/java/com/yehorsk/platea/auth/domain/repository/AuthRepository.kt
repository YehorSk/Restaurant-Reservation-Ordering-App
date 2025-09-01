package com.yehorsk.platea.auth.domain.repository

import android.app.Activity
import androidx.credentials.GetCredentialResponse
import com.yehorsk.platea.auth.data.remote.model.AuthDataDto
import com.yehorsk.platea.auth.presentation.forgot.ForgotFormState
import com.yehorsk.platea.auth.presentation.login.LoginForm
import com.yehorsk.platea.auth.presentation.register.RegisterForm
import com.yehorsk.platea.core.domain.remote.AppError
import com.yehorsk.platea.core.domain.remote.Result

interface AuthRepository {
    suspend fun register(registerForm: RegisterForm, activity: Activity) : Result<List<AuthDataDto>, AppError>

    suspend fun login(loginForm: LoginForm,withoutCredentials: Boolean = true, activity: Activity) : Result<List<AuthDataDto>, AppError>

    suspend fun loginWithSavedCredentials(activity: Activity) : Result<List<AuthDataDto>, AppError>

    suspend fun handleAuthResult(result: GetCredentialResponse, activity: Activity) : Result<List<AuthDataDto>, AppError>

    suspend fun authenticate() : Result<List<AuthDataDto>, AppError>

    suspend fun logout() : Result<List<AuthDataDto>, AppError>

    suspend fun forgotPassword(forgotFormState: ForgotFormState) : Result<List<String>, AppError>

    suspend fun setLocale(lang: String) : Result<List<AuthDataDto>, AppError>
}