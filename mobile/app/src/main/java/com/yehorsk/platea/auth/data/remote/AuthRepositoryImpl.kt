package com.yehorsk.platea.auth.data.remote

import android.app.Activity
import android.content.Context
import androidx.credentials.CreatePasswordRequest
import androidx.credentials.CreatePasswordResponse
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.GetPasswordOption
import androidx.credentials.PasswordCredential
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import com.google.firebase.Firebase
import com.google.firebase.messaging.messaging
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
import com.yehorsk.platea.core.utils.Utility
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authService: AuthService,
    private val prefs: MainPreferencesRepository,
    private val mainRoomDatabase: MainRoomDatabase,
    private val credentialManager: CredentialManager,
    private val context: Context
) : AuthRepository {

    override suspend fun register(
        registerForm: RegisterForm,
        activity: Activity
    ): Result<List<AuthDataDto>, AppError> {
        Timber.d("Auth register")
        val fcmToken = Firebase.messaging.token.await()
        val deviceId = Utility.getDeviceId(context)
        val deviceType = "android"
        val updatedRegisterForm = registerForm.copy(
            fcmToken = fcmToken,
            deviceId = deviceId,
            deviceType = deviceType
        )
        return safeCall<AuthDataDto>(
            execute = {
                authService.register(updatedRegisterForm)
            }
        ).onSuccess { data, _ ->
            prefs.saveUser(data.first())

            try {
                credentialManager.createCredential(
                    context = activity,
                    request = CreatePasswordRequest(
                        id = data.first().user!!.email!!,
                        password = registerForm.password
                    )
                )
            } catch (e: Exception) {
                Timber.d("Credential creation cancelled or failed: ${e.localizedMessage}")
            }
        }
    }

    override suspend fun login(
        loginForm: LoginForm,
        withoutCredentials: Boolean,
        activity: Activity
    ): Result<List<AuthDataDto>, AppError> {
        Timber.d("Auth login")
        val fcmToken = Firebase.messaging.token.await()
        val deviceId = Utility.getDeviceId(context)
        val deviceType = "android"
        val updatedLoginForm = loginForm.copy(
            fcmToken = fcmToken,
            deviceId = deviceId,
            deviceType = deviceType
        )
        return safeCall<AuthDataDto>(
            execute = {
                authService.login(updatedLoginForm)
            }
        ).onSuccess { data, _ ->
            prefs.saveUser(data.first())
            if(withoutCredentials){
                try {
                    credentialManager.createCredential(
                        context = activity,
                        request = CreatePasswordRequest(
                            id = data.first().user!!.email!!,
                            password = loginForm.password
                        )
                    )
                } catch (e: Exception) {
                    Timber.d("Credential creation cancelled or failed: ${e.localizedMessage}")
                }
            }
        }.onError { error ->
            if (error == AppError.UNAUTHORIZED) {
                prefs.clearAllTokens()
            }
        }
    }

    override suspend fun loginWithSavedCredentials(
        activity: Activity
    ): Result<List<AuthDataDto>, AppError> {
        Timber.d("Auth loginWithSavedCredentials")
        return try {
            val credentialResponse = credentialManager.getCredential(
                activity,
                request = GetCredentialRequest(
                    credentialOptions = listOf(GetPasswordOption())
                )
            )
            handleAuthResult(credentialResponse, activity)
        } catch (e: GetCredentialCancellationException) {
            Result.Error(AppError.CANCELLED)
        } catch (e: GetCredentialException) {
            Result.Error(AppError.NO_CREDENTIAL)
        } catch (e: Exception) {
            Result.Error(AppError.UNKNOWN_ERROR)
        }
    }

    override suspend fun handleAuthResult(
        result: GetCredentialResponse,
        activity: Activity
    ): Result<List<AuthDataDto>, AppError> {
        return when(val credential = result.credential){
            is PasswordCredential ->{
                val fcmToken = Firebase.messaging.token.await()
                val deviceId = Utility.getDeviceId(context)
                val deviceType = "android"
                login(
                    LoginForm(
                        email = credential.id,
                        password = credential.password,
                        fcmToken = fcmToken,
                        deviceId = deviceId,
                        deviceType = deviceType
                    ),
                    withoutCredentials = false,
                    activity = activity
                )
            }
            else ->{
                Result.Error(AppError.UNAUTHORIZED)
            }
        }
    }

    override suspend fun authenticate(): Result<List<AuthDataDto>, AppError> {
        Timber.d("Auth authenticate")
        val fcmToken = Firebase.messaging.token.await()
        val deviceId = Utility.getDeviceId(context)
        val deviceType = "android"
        val authState = AuthState(fcmToken, deviceId, deviceType)
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