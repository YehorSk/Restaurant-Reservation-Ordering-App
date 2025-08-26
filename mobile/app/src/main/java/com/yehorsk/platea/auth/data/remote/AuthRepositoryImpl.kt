package com.yehorsk.platea.auth.data.remote

import com.yehorsk.platea.auth.data.remote.model.AuthDataDto
import com.yehorsk.platea.auth.data.remote.service.AuthService
import com.yehorsk.platea.auth.domain.repository.AuthRepository
import com.yehorsk.platea.auth.presentation.forgot.ForgotFormState
import com.yehorsk.platea.auth.presentation.login.AuthState
import com.yehorsk.platea.auth.presentation.login.LoginForm
import com.yehorsk.platea.auth.presentation.register.RegisterForm
import com.yehorsk.platea.core.data.db.MainRoomDatabase
import com.yehorsk.platea.core.data.remote.service.safeCall
import com.yehorsk.platea.core.data.repository.MainPreferencesRepository
import com.yehorsk.platea.core.domain.remote.AppError
import com.yehorsk.platea.core.domain.remote.Result
import com.yehorsk.platea.core.domain.remote.onError
import com.yehorsk.platea.core.domain.remote.onSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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
            }
        ).onSuccess { data, _ ->
            prefs.saveUser(data.first())
        }
    }

    override suspend fun login(loginForm: LoginForm): Result<List<AuthDataDto>, AppError> {
        Timber.d("Auth login")
        return safeCall<AuthDataDto>(
            execute = {
                authService.login(loginForm)
            }
        ).onSuccess { data, _ ->
            prefs.saveUser(data.first())
        }.onError { error ->
            if (error == AppError.UNAUTHORIZED) {
                prefs.clearAllTokens()
            }
        }
    }

    override suspend fun authenticate(authState: AuthState): Result<List<AuthDataDto>, AppError> {
        Timber.d("Auth authenticate")
        return safeCall<AuthDataDto>(
            execute = {
                authService.authenticate(authState)
            }
        ).onSuccess { data, _ ->
            prefs.saveUser(data.first())
        }.onError { error ->
            if (error == AppError.UNAUTHORIZED) {
                prefs.clearAllTokens()
                withContext(Dispatchers.IO) {
                    mainRoomDatabase.clearAllTables()
                }
            }
        }
    }

    override suspend fun logout(): Result<List<AuthDataDto>, AppError> {
        Timber.d("Auth logout")
        return safeCall<AuthDataDto>(
            execute = {
                authService.logout()
            }
        ).onSuccess { data, _ ->
            prefs.clearAllTokens()
            withContext(Dispatchers.IO) {
                mainRoomDatabase.clearAllTables()
            }
        }.onError { error ->
            when (error) {
                AppError.UNAUTHORIZED -> {
                    prefs.clearAllTokens()
                    withContext(Dispatchers.IO) {
                        mainRoomDatabase.clearAllTables()
                    }
                }
                else -> {}
            }
        }
    }

    override suspend fun forgotPassword(forgotFormState: ForgotFormState): Result<List<String>, AppError> {
        Timber.d("Auth forgotPassword")
        return safeCall<String>(
            execute = {
                authService.forgotPassword(forgotFormState)
            }
        )
    }

    override suspend fun setLocale(lang: String): Result<List<AuthDataDto>, AppError> {
        Timber.d("Auth setLocale")
        return safeCall<AuthDataDto>(
            execute = {
                authService.setLocale(lang)
            }
        )
    }

}