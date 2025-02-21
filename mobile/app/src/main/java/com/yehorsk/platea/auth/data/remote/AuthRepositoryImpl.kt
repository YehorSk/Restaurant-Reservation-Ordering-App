package com.yehorsk.platea.auth.data.remote

import androidx.compose.ui.text.intl.Locale
import com.yehorsk.platea.auth.data.remote.model.AuthDataDto
import com.yehorsk.platea.auth.data.repository.AuthRepository
import com.yehorsk.platea.auth.data.service.AuthService
import com.yehorsk.platea.auth.presentation.forgot.ForgotFormState
import com.yehorsk.platea.auth.presentation.login.AuthState
import com.yehorsk.platea.auth.presentation.login.LoginForm
import com.yehorsk.platea.auth.presentation.register.RegisterForm
import com.yehorsk.platea.core.data.db.MainRoomDatabase
import com.yehorsk.platea.core.data.remote.safeCall
import com.yehorsk.platea.core.data.repository.MainPreferencesRepository
import com.yehorsk.platea.core.domain.remote.AppError
import com.yehorsk.platea.core.domain.remote.Result
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
            },
            onSuccess = {

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
            },
            onFailure = { error ->
                if (error == AppError.UNAUTHORIZED) {
                    prefs.clearAllTokens()
                }
            }
        )
    }

    override suspend fun authenticate(authState: AuthState): Result<List<AuthDataDto>, AppError> {
        Timber.d("Auth authenticate START")
        val totalStart = System.currentTimeMillis()

        val httpStart = System.currentTimeMillis()
        val result = safeCall<AuthDataDto>(
            execute = {
                authService.authenticate(authState)
            },
            onSuccess = { result ->
                val successStart = System.currentTimeMillis()
                Timber.d("Auth HTTP request took ${successStart - httpStart} ms")
                prefs.saveUser(result.first())
                Timber.d("Auth prefs.saveUser() took ${System.currentTimeMillis() - successStart} ms")
//                setLocale(Locale.current.language)
//                Timber.d("Auth setLocale() took ${System.currentTimeMillis() - successStart} ms")
            },
            onFailure = { error ->
                val failureStart = System.currentTimeMillis()
                if (error == AppError.UNAUTHORIZED) {
                    prefs.clearAllTokens()
                    withContext(Dispatchers.IO) {
                        mainRoomDatabase.clearAllTables()
                    }
                }
                Timber.d("Auth Failure handling took ${System.currentTimeMillis() - failureStart} ms")
            }
        )

        Timber.d("Auth authenticate() took ${System.currentTimeMillis() - totalStart} ms")
        return result
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
            },
            onFailure = { error ->
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
        )
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