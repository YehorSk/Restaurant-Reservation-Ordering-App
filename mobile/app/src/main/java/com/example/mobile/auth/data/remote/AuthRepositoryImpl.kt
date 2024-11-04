package com.example.mobile.auth.data.remote

import com.example.mobile.auth.data.remote.model.AuthResult
import com.example.mobile.auth.data.remote.model.HttpResponse
import com.example.mobile.auth.data.repository.AuthRepository
import com.example.mobile.auth.data.service.AuthService
import com.example.mobile.auth.presentation.login.LoginForm
import com.example.mobile.auth.presentation.register.RegisterForm
import com.example.mobile.core.data.db.MainRoomDatabase
import com.example.mobile.core.data.repository.MainPreferencesRepository
import com.example.mobile.utils.ConnectivityRepository
import com.example.mobile.utils.parseHttpResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authService: AuthService,
    private val prefs: MainPreferencesRepository,
    private val connectivityRepository: ConnectivityRepository,
    private val mainRoomDatabase: MainRoomDatabase
) : AuthRepository {

    override suspend fun register(registerForm: RegisterForm): AuthResult<HttpResponse> {
        val isOnline = connectivityRepository.isInternetConnected()
        return if(isOnline){
            try{
                val result = authService.register(registerForm)
                login(LoginForm(email = registerForm.email, password = registerForm.password))
                AuthResult.Authorized(result)
            }catch (e: HttpException) {
                if (e.code() == 422) {
                    val responseBody = e.response()?.errorBody()?.string() ?: ""
                    val httpResponse = parseHttpResponse(responseBody)
                    AuthResult.Unauthorized(httpResponse)
                } else {
                    AuthResult.UnknownError(HttpResponse(null, e.message(), null))
                }
            }
        }else{
            AuthResult.UnknownError(HttpResponse(null, "No internet connection!", null))
        }
    }

    override suspend fun login(loginForm: LoginForm): AuthResult<HttpResponse> {
        val isOnline = connectivityRepository.isInternetConnected()
        return if(isOnline){
            try{
                val result = authService.login(loginForm)
                prefs.saveUser(result)
                Timber.tag("USER").d(result.toString())
                AuthResult.Authorized(result)
            }catch (e: HttpException) {
                if (e.code() == 401) {
                    val responseBody = e.response()?.errorBody()?.string() ?: ""
                    val httpResponse = parseHttpResponse(responseBody)
                    AuthResult.Unauthorized(httpResponse)
                } else {
                    AuthResult.UnknownError(HttpResponse(null, e.message(), null))
                }
            }
        }else{
            AuthResult.UnknownError(HttpResponse(null, "No internet connection!", null))
        }
    }

    override suspend fun authenticate(): AuthResult<HttpResponse> {
        val isOnline = connectivityRepository.isInternetConnected()
        return if(isOnline){
            try{
                val token = prefs.jwtTokenFlow.first()
                if (token.isNullOrBlank()) {
                    return AuthResult.Unauthorized(HttpResponse(message = "Unauthenticated"))
                }
                val result = authService.authenticate()
                AuthResult.Authorized(result)
            }catch (e: HttpException) {
                if (e.code() == 401) {
                    prefs.clearAllTokens()
                    AuthResult.Unauthorized(HttpResponse(message = e.message()))
                } else {
                    AuthResult.UnknownError(HttpResponse(message = e.code().toString()))
                }
            }
        }else{
            AuthResult.UnknownError(HttpResponse(null, "No internet connection!", null))
        }
    }

    override suspend fun logout(): AuthResult<HttpResponse> {
        val isOnline = connectivityRepository.isInternetConnected()
        return if(isOnline){
            try{
                val result = authService.logout()
                prefs.clearAllTokens()
                withContext(Dispatchers.IO) {
                    mainRoomDatabase.clearAllTables()
                }
                AuthResult.Unauthorized(result)
            }catch (e: HttpException) {
                if (e.code() == 401) {
                    AuthResult.Unauthorized(HttpResponse(message = e.message()))
                } else {
                    AuthResult.UnknownError(HttpResponse(message = e.code().toString()))
                }
            }
        }else{
            AuthResult.UnknownError(HttpResponse(null, "No internet connection!", null))
        }
    }

}