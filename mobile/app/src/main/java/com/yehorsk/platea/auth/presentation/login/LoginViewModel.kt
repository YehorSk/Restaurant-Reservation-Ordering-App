package com.yehorsk.platea.auth.presentation.login

import android.content.Context
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
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    authRepository: AuthRepository,
    preferencesRepository: MainPreferencesRepository,
    networkConnectivityObserver: ConnectivityObserver,
    @ApplicationContext context: Context
): BaseAuthViewModel(authRepository, preferencesRepository, networkConnectivityObserver, context) {

    private val _uiState = MutableStateFlow(LoginState())
    val uiState: StateFlow<LoginState> = _uiState.asStateFlow()

    val userRole: StateFlow<String?> = preferencesRepository.userRoleFlow
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    val userToken: StateFlow<String?> = preferencesRepository.jwtTokenFlow
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    init {
        viewModelScope.launch(Dispatchers.Main){
            authenticate()
        }
    }

    fun updateLogUiState(loginForm: LoginForm){
        _uiState.update { state ->
            state.copy(
                loginForm = loginForm,
                isEntryValid = validateLogInput(state)
            )
        }
    }

    private fun authenticateOffline(){
        Timber.d("Auth authenticateOffline")
        viewModelScope.launch {
            _uiState.update { it.copy(
                isLoading = true,
                isAuthenticating = true
            )
            }
            preferencesRepository.jwtTokenFlow.collect { token ->
                Timber.tag("NetworkCheck ").v("checkIfLoggedIn Token received: $token")
                val isLoggedIn = token != null
                _uiState.update { currentState ->
                    currentState.copy(isLoading = false, isAuthenticating = false, isLoggedIn = isLoggedIn)
                }
            }
        }
    }

    private fun validateLogInput(uiState: LoginState): Boolean{
        return with(uiState.loginForm){
            email.isNotBlank() && password.isNotBlank()
        }
    }

    private fun authenticate() {
        viewModelScope.launch {
            _uiState.update { it.copy(
                    isLoading = true,
                    isAuthenticating = true
                )
            }
            val fcmToken = Firebase.messaging.token.await()
            val deviceId = Utility.getDeviceId(context)
            val deviceType = "android"
            val authState = AuthState(
                fcmToken = fcmToken,
                deviceId = deviceId,
                deviceType = deviceType
            )
            authRepository.authenticate(authState)
                .onSuccess { data, _ ->
                _uiState.update { it.copy(isLoading = false,isAuthenticating = false, isLoggedIn = true) }
            }.onError { error ->
                when (error) {
                    AppError.UNAUTHORIZED -> {
                        _uiState.update { it.copy(isLoading = false,isAuthenticating = false , isLoggedIn = false) }
                    }
                    AppError.NO_INTERNET -> {
                        authenticateOffline()
                    }
                    else -> {
                        _uiState.update { it.copy(isLoading = false,isAuthenticating = false , isLoggedIn = false) }
                    }
                }
            }
        }
    }


    fun login() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val fcmToken = Firebase.messaging.token.await()
            val deviceId = Utility.getDeviceId(context)
            val deviceType = "android"
            _uiState.update { state ->
                val updatedLoginForm = state.loginForm.copy(
                    fcmToken = fcmToken,
                    deviceId = deviceId,
                    deviceType = deviceType
                )
                state.copy(loginForm = updatedLoginForm)
            }
            val result = authRepository.login(loginForm = uiState.value.loginForm)

            result.onSuccess { data, message ->
                _sideEffectChannel.send(SideEffect.ShowSuccessToast(message.toString()))
                _uiState.update { it.copy(isLoading = false, isLoggedIn = true) }
            }.onError { error ->
                when (error) {
                    AppError.UNAUTHORIZED -> {
                        _sideEffectChannel.send(SideEffect.ShowErrorToast(error))
                        _uiState.update { it.copy(isLoading = false, isLoggedIn = false) }
                    }
                    else -> {
                        _sideEffectChannel.send(SideEffect.ShowErrorToast(error))
                        _uiState.update { it.copy(isLoading = false) }
                    }
                }
            }
        }
    }

}