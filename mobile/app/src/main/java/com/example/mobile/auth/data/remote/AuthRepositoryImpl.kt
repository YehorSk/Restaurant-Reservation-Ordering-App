package com.example.mobile.auth.data.remote

import androidx.compose.ui.text.intl.Locale
import com.example.mobile.auth.data.remote.model.AuthDataDto
import com.example.mobile.auth.data.repository.AuthRepository
import com.example.mobile.auth.data.service.AuthService
import com.example.mobile.auth.presentation.login.LoginForm
import com.example.mobile.auth.presentation.register.RegisterForm
import com.example.mobile.core.data.db.MainRoomDatabase
import com.example.mobile.core.data.remote.safeCall
import com.example.mobile.core.data.repository.MainPreferencesRepository
import com.example.mobile.core.domain.remote.AppError
import com.example.mobile.core.domain.remote.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authService: AuthService,
    private val prefs: MainPreferencesRepository,
    private val mainRoomDatabase: MainRoomDatabase
) : AuthRepository {

    override suspend fun register(registerForm: RegisterForm): Result<List<AuthDataDto>, AppError> {
        Timber.d("Auth register")
        return safeCall<AuthDataDto>(
            execute = {
                authService.register(registerForm)
            },
            onSuccess = {
                login(LoginForm(email = registerForm.email, password = registerForm.password))
            }
        )
    }

    override suspend fun login(loginForm: LoginForm): Result<List<AuthDataDto>, AppError> {
        Timber.d("Auth login")
        return safeCall<AuthDataDto>(
            execute = {
                authService.login(loginForm)
            },
            onSuccess = { result ->
                prefs.saveUser(result.first())
                setLocale(Locale.current.language)
            }
        )
    }

    override suspend fun authenticate(): Result<List<AuthDataDto>, AppError> {
        Timber.d("Auth login")
        return safeCall<AuthDataDto>(
            execute = {
                authService.authenticate()
            },
            onSuccess = { result ->
                prefs.saveUser(result.first())
                setLocale(Locale.current.language)
            },
            onFailure = { error ->
                if (error == AppError.UNAUTHORIZED) {
                    prefs.clearAllTokens()
                }
            }
        )
    }

    override suspend fun logout(): Result<List<AuthDataDto>, AppError> {
        Timber.d("Auth logout")
        return safeCall<AuthDataDto>(
            execute = {
                authService.logout()
            },
            onSuccess = { result ->
                prefs.clearAllTokens()
                withContext(Dispatchers.IO) {
                    mainRoomDatabase.clearAllTables()
                }
            }
        )
    }

    override suspend fun setLocale(lang: String): Result<List<String>, AppError> {
        Timber.d("Auth setLocale")
        return safeCall<String>(
            execute = {
                authService.setLocale(lang)
            },
            onSuccess = { result ->
                prefs.clearAllTokens()
                withContext(Dispatchers.IO) {
                    mainRoomDatabase.clearAllTables()
                }
            }
        )
    }

}