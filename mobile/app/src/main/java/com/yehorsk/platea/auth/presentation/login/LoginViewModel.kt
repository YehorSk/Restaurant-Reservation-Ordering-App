package com.yehorsk.platea.auth.presentation.login

import android.app.Activity
import androidx.lifecycle.viewModelScope
import com.yehorsk.platea.auth.domain.repository.AuthRepository
import com.yehorsk.platea.auth.presentation.BaseAuthViewModel
import com.yehorsk.platea.core.data.repository.MainPreferencesRepository
import com.yehorsk.platea.core.domain.remote.AppError
import com.yehorsk.platea.core.domain.remote.onError
import com.yehorsk.platea.core.domain.remote.onSuccess
import com.yehorsk.platea.core.utils.ConnectivityObserver
import com.yehorsk.platea.core.utils.snackbar.SnackbarController
import com.yehorsk.platea.core.utils.snackbar.SnackbarEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    authRepository: AuthRepository,
    preferencesRepository: MainPreferencesRepository,
    networkConnectivityObserver: ConnectivityObserver
): BaseAuthViewModel(authRepository, preferencesRepository, networkConnectivityObserver) {

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

    fun loginWithSavedCredentials(activity: Activity) {
        Timber.d("Auth loginWithSavedCredentials")
        _uiState.update { it.copy(
            isGoogleAuth = true
        ) }
        viewModelScope.launch {
            authRepository.loginWithSavedCredentials(activity)
                .onSuccess { data, _ ->
                    _uiState.update { it.copy(
                            isLoggedIn = true,
                            isGoogleAuth = false
                        )
                    }
                }
                .onError { error ->
                    SnackbarController.sendEvent(
                        event = SnackbarEvent(
                            error = error
                        )
                    )
                    _uiState.update { it.copy(
                            isGoogleAuth = false
                        )
                    }
            }
        }
    }

    private fun authenticate() {
        viewModelScope.launch {
            _uiState.update { it.copy(
                    isLoading = true,
                    isAuthenticating = true,
                    isLoggedIn = false
                )
            }
            val result = authRepository.authenticate()
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

    fun login(activity: Activity) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val result = authRepository.login(loginForm = uiState.value.loginForm, activity = activity)
            result.onSuccess { data, message ->
                _uiState.update { it.copy(isLoading = false, isLoggedIn = true) }
            }.onError { error ->
                _uiState.update { it.copy(isLoading = false, isLoggedIn = false) }
                SnackbarController.sendEvent(
                    event = SnackbarEvent(
                        error = error
                    )
                )
            }
        }
    }

}