package com.yehorsk.platea.auth.presentation.register

import android.app.Application
import android.content.Context
import android.provider.Settings
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.messaging.messaging
import com.yehorsk.platea.auth.data.repository.AuthRepository
import com.yehorsk.platea.auth.presentation.BaseAuthViewModel
import com.yehorsk.platea.core.data.repository.MainPreferencesRepository
import com.yehorsk.platea.core.domain.remote.AppError
import com.yehorsk.platea.core.domain.remote.SideEffect
import com.yehorsk.platea.core.domain.remote.onError
import com.yehorsk.platea.core.domain.remote.onSuccess
import com.yehorsk.platea.core.utils.ConnectivityObserver
import com.yehorsk.platea.core.utils.Utility
import com.yehorsk.platea.core.utils.cleanError
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    authRepository: AuthRepository,
    preferencesRepository: MainPreferencesRepository,
    networkConnectivityObserver: ConnectivityObserver,
    @ApplicationContext context: Context
): BaseAuthViewModel(authRepository, preferencesRepository, networkConnectivityObserver, context) {

    private val _uiState = MutableStateFlow(RegisterState())
    val uiState: StateFlow<RegisterState> = _uiState.asStateFlow()

    private fun validateRegInput(uiState: RegisterState): Boolean{
        return with(uiState.registerForm){
            name.isNotBlank() && email.isNotBlank() && password.isNotBlank() && passwordConfirm.isNotBlank()
        }
    }

    fun updateRegUiState(registerForm: RegisterForm){
        _uiState.update { state ->
            state.copy(
                registerForm = registerForm,
                isEntryValid = validateRegInput(state)
            )
        }
    }

    fun register() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val fcmToken = Firebase.messaging.token.await()
            val deviceId = Utility.getDeviceId(context)
            val deviceType = "android"
            _uiState.update { state ->
                val updatedRegisterForm = state.registerForm.copy(
                    fcmToken = fcmToken,
                    deviceId = deviceId,
                    deviceType = deviceType
                )
                state.copy(registerForm = updatedRegisterForm)
            }
            authRepository.register(registerForm = uiState.value.registerForm)
                .onSuccess { data, _ ->
                Timber.tag("Authorized").v(data.toString())
                preferencesRepository.saveUser(data.first())
                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        isLoggedIn = true,
                        registerFormErrors = RegisterFormErrors(
                            email = "",
                            password = "",
                            passwordConfirm = "",
                            name = ""
                        )
                    )
                }
            }.onError { error ->
                when (error) {
                    AppError.UNAUTHORIZED -> {
                        preferencesRepository.clearAllTokens()
                        _uiState.update { currentState ->
                            currentState.copy(
                                isLoggedIn = false,
                                isLoading = false,
                                registerFormErrors = RegisterFormErrors(
                                    email = cleanError(uiState.value.registerForm.email),
                                    password = cleanError(uiState.value.registerForm.password),
                                    passwordConfirm = cleanError(uiState.value.registerForm.passwordConfirm),
                                    name = cleanError(uiState.value.registerForm.name)
                                )
                            )
                        }
                    }
                    AppError.UNKNOWN_ERROR -> {
                        _uiState.update { it.copy(isLoading = false) }
                    }
                    AppError.INCORRECT_DATA -> {
                        _uiState.update { it.copy(isLoading = false) }
                        _sideEffectChannel.send(SideEffect.ShowErrorToast(error))
                    }
                    else -> {
                        _uiState.update { it.copy(isLoading = false) }
                        Timber.tag("UnhandledError").e(error.toString())
                    }
                }
            }
        }
    }


}