package com.yehorsk.platea.auth.presentation.login

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.messaging.messaging
import com.yehorsk.platea.auth.domain.repository.AuthRepository
import com.yehorsk.platea.auth.presentation.BaseAuthViewModel
import com.yehorsk.platea.core.data.repository.MainPreferencesRepository
import com.yehorsk.platea.core.domain.remote.AppError
import com.yehorsk.platea.core.domain.remote.onError
import com.yehorsk.platea.core.domain.remote.onSuccess
import com.yehorsk.platea.core.utils.ConnectivityObserver
import com.yehorsk.platea.core.utils.SideEffect
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

    init {
        viewModelScope.launch(Dispatchers.Main){
            if(networkConnectivityObserver.isAvailable){
                Timber.d("Auth online")
                authenticate()
            }else{
                Timber.d("Auth offline")
                authenticateOffline()
            }
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
            val startTime = System.currentTimeMillis()
            Timber.d("Auth state $authState")
            val result = authRepository.authenticate(authState)
            val duration = System.currentTimeMillis() - startTime
            Timber.d("authenticate() took $duration ms")
            result
                .onSuccess { data, _ ->
                _uiState.update { it.copy(isLoading = false,isAuthenticating = false, isLoggedIn = true) }
            }.onError { error ->
                when (error) {
                    AppError.UNAUTHORIZED -> {
                        _uiState.update { it.copy(isLoading = false,isAuthenticating = false , isLoggedIn = false) }
                    }
                    else -> {
                        authenticateOffline()
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
            Timber.d("Auth state ${uiState.value.loginForm.toString()}")
            val result = authRepository.login(loginForm = uiState.value.loginForm)

            result.onSuccess { data, message ->
                _uiState.update { it.copy(isLoading = false, isLoggedIn = true) }
            }.onError { error ->
                _uiState.update { it.copy(isLoading = false, isLoggedIn = false) }
                _sideEffectChannel.send(SideEffect.ShowErrorToast(error))
            }
        }
    }

}