package com.example.mobile.auth.data.service

import com.example.mobile.auth.data.remote.model.HttpResponse
import com.example.mobile.auth.presentation.login.LoginForm
import com.example.mobile.auth.presentation.register.RegisterForm
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface AuthService {

    @Headers(
        "Accept: application/vnd.api+json",
        "Content-Type: application/vnd.api+json"
    )
    @POST("register")
    suspend fun register(@Body registerForm: RegisterForm) : HttpResponse

    @POST("login")
    suspend fun login(@Body loginForm: LoginForm) : HttpResponse

    @POST("logout")
    suspend fun logout() : HttpResponse

    @GET("user")
    suspend fun authenticate() : HttpResponse

}