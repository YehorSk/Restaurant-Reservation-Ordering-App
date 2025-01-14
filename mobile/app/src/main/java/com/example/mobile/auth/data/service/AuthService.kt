package com.example.mobile.auth.data.service

import com.example.mobile.auth.data.remote.model.AuthDataDto
import com.example.mobile.auth.presentation.login.LoginForm
import com.example.mobile.auth.presentation.register.RegisterForm
import com.example.mobile.core.data.remote.dto.ResponseDto
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
    @GET("user")
    suspend fun authenticate() : ResponseDto<AuthDataDto>

    @Headers(
        "Accept: application/vnd.api+json",
        "Content-Type: application/vnd.api+json"
    )
    @GET("locale/{locale}")
    suspend fun setLocale(@Path("locale") lang: String) : ResponseDto<String>

}