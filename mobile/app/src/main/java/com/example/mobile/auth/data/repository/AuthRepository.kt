package com.example.mobile.auth.data.repository

import com.example.mobile.auth.data.remote.model.AuthResult
import com.example.mobile.auth.data.remote.model.HttpResponse
import com.example.mobile.auth.presentation.login.LoginForm
import com.example.mobile.auth.presentation.register.RegisterForm

interface AuthRepository {
    suspend fun register(registerForm: RegisterForm) : AuthResult<HttpResponse>

    suspend fun login(loginForm: LoginForm) : AuthResult<HttpResponse>

    suspend fun authenticate() : AuthResult<HttpResponse>

    suspend fun logout() : AuthResult<HttpResponse>
}