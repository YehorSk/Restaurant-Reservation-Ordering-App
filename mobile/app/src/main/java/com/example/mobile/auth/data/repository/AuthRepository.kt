package com.example.mobile.auth.data.repository

import com.example.mobile.auth.data.remote.model.AuthDataDto
import com.example.mobile.auth.presentation.forgot.ForgotFormState
import com.example.mobile.auth.presentation.login.LoginForm
import com.example.mobile.auth.presentation.register.RegisterForm
import com.example.mobile.core.domain.remote.AppError
import com.example.mobile.core.domain.remote.Result

interface AuthRepository {
    suspend fun register(registerForm: RegisterForm) : Result<List<AuthDataDto>, AppError>

    suspend fun login(loginForm: LoginForm) : Result<List<AuthDataDto>, AppError>

    suspend fun authenticate() : Result<List<AuthDataDto>, AppError>

    suspend fun logout() : Result<List<AuthDataDto>, AppError>

    suspend fun forgotPassword(forgotFormState: ForgotFormState) : Result<List<String>, AppError>

    suspend fun setLocale(lang: String) : Result<List<String>, AppError>
}