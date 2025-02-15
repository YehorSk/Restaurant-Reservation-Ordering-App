package com.yehorsk.platea.auth.data.service

import com.yehorsk.platea.auth.data.remote.model.AuthDataDto
import com.yehorsk.platea.auth.presentation.forgot.ForgotFormState
import com.yehorsk.platea.auth.presentation.login.AuthState
import com.yehorsk.platea.auth.presentation.login.LoginForm
import com.yehorsk.platea.auth.presentation.register.RegisterForm
import com.yehorsk.platea.core.data.remote.dto.ResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface AuthService {

    @POST("register")
    suspend fun register(@Body registerForm: RegisterForm) : ResponseDto<AuthDataDto>

    @POST("login")
    suspend fun login(@Body loginForm: LoginForm) : ResponseDto<AuthDataDto>

    @POST("logout")
    suspend fun logout() : ResponseDto<AuthDataDto>

    @POST("user")
    suspend fun authenticate(@Body authState: AuthState) : ResponseDto<AuthDataDto>

    @POST("forgot-password")
    suspend fun forgotPassword(@Body forgotFormState: ForgotFormState) : ResponseDto<String>

    @GET("update-language/{language}")
    suspend fun setLocale(@Path("language") language: String) : ResponseDto<AuthDataDto>

}