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

    @Headers(
        "Accept: application/vnd.api+json",
        "Content-Type: application/vnd.api+json"
    )
    @POST("register")
    suspend fun register(@Body registerForm: RegisterForm) : ResponseDto<AuthDataDto>

    @Headers(
        "Accept: application/vnd.api+json",
        "Content-Type: application/vnd.api+json"
    )
    @POST("login")
    suspend fun login(@Body loginForm: LoginForm) : ResponseDto<AuthDataDto>

    @Headers(
        "Accept: application/vnd.api+json",
        "Content-Type: application/vnd.api+json"
    )
    @POST("logout")
    suspend fun logout() : ResponseDto<AuthDataDto>

    @Headers(
        "Accept: application/vnd.api+json",
        "Content-Type: application/vnd.api+json"
    )
    @POST("user")
    suspend fun authenticate(@Body authState: AuthState) : ResponseDto<AuthDataDto>

    @Headers(
        "Accept: application/vnd.api+json",
        "Content-Type: application/vnd.api+json"
    )
    @POST("forgot-password")
    suspend fun forgotPassword(@Body forgotFormState: ForgotFormState) : ResponseDto<String>

    @Headers(
        "Accept: application/vnd.api+json",
        "Content-Type: application/vnd.api+json"
    )
    @GET("locale/{locale}")
    suspend fun setLocale(@Path("locale") lang: String) : ResponseDto<String>

}