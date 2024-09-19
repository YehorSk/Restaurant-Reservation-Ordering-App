package com.example.mobile.auth.data.repository

import com.example.mobile.auth.data.model.AuthResult
import com.example.mobile.auth.data.model.HttpResponse
import com.example.mobile.ui.screens.auth.login.LoginForm
import com.example.mobile.ui.screens.auth.register.RegisterForm

interface AuthRepository {
    suspend fun register(registerForm: RegisterForm) : AuthResult<HttpResponse>

    suspend fun login(loginForm: LoginForm) : AuthResult<HttpResponse>

    suspend fun authenticate() : AuthResult<HttpResponse>

    suspend fun logout() : AuthResult<HttpResponse>
}