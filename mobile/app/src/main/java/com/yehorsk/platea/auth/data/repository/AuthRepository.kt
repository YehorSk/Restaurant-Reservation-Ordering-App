package com.yehorsk.platea.auth.data.repository

import com.yehorsk.platea.auth.data.remote.model.AuthDataDto
import com.yehorsk.platea.auth.presentation.forgot.ForgotFormState
import com.yehorsk.platea.auth.presentation.login.AuthState
import com.yehorsk.platea.auth.presentation.login.LoginForm
import com.yehorsk.platea.auth.presentation.register.RegisterForm
import com.yehorsk.platea.core.domain.remote.AppError
import com.yehorsk.platea.core.domain.remote.Result

interface AuthRepository {
    suspend fun register(registerForm: RegisterForm) : Result<List<AuthDataDto>, AppError>

    suspend fun login(loginForm: LoginForm) : Result<List<AuthDataDto>, AppError>

    suspend fun authenticate(authState: AuthState) : Result<List<AuthDataDto>, AppError>

    suspend fun logout() : Result<List<AuthDataDto>, AppError>

    suspend fun forgotPassword(forgotFormState: ForgotFormState) : Result<List<String>, AppError>

    suspend fun setLocale(lang: String) : Result<List<AuthDataDto>, AppError>
}