package com.yehorsk.platea.auth.presentation.login

import androidx.lifecycle.viewModelScope
import com.yehorsk.platea.auth.data.repository.AuthRepository
import com.yehorsk.platea.auth.presentation.BaseAuthViewModel
import com.yehorsk.platea.core.data.repository.MainPreferencesRepository
import com.yehorsk.platea.core.domain.remote.AppError
import com.yehorsk.platea.core.domain.remote.SideEffect
import com.yehorsk.platea.core.domain.remote.onError
import com.yehorsk.platea.core.domain.remote.onSuccess
import com.yehorsk.platea.core.utils.ConnectivityObserver
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
    networkConnectivityObserver: ConnectivityObserver,
): BaseAuthViewModel(authRepository, preferencesRepository, networkConnectivityObserver) {

    private val _uiState = MutableStateFlow(LoginState())
    val uiState: StateFlow<LoginState> = _uiState.asStateFlow()

    val userRole: StateFlow<String?> = preferencesRepository.userRoleFlow
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    val userToken: StateFlow<String?> = preferencesRepository.jwtTokenFlow
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    init {
        viewModelScope.launch(Dispatchers.Main){
            isNetwork.collect{ available ->
                if (available == true && userToken.toString().isNotEmpty()){
                    authenticate()
                }else{
                    checkIfLoggedIn()
                }
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

    private fun checkIfLoggedIn(){
        viewModelScope.launch {
            preferencesRepository.jwtTokenFlow.collect { token ->
                Timber.tag("NetworkCheck ").v("checkIfLoggedIn Token received: $token")
                val isLoggedIn = token != null
                _uiState.update { currentState ->
                    currentState.copy(isLoggedIn = isLoggedIn)
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
            _uiState.update { it.copy(isLoading = true) }

            authRepository.authenticate()
                .onSuccess { data, _ ->
                preferencesRepository.saveUser(data.first())
                _uiState.update { it.copy(isLoading = false, isLoggedIn = true) }
            }.onError { error ->
                when (error) {
                    AppError.UNAUTHORIZED -> {
                        preferencesRepository.clearAllTokens()
                        _uiState.update { it.copy(isLoading = false, isLoggedIn = false) }
                    }
                    else -> {
                        _uiState.update { it.copy(isLoading = false, isLoggedIn = false) }
                    }
                }
            }
        }
    }


    fun login() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val result = authRepository.login(loginForm = uiState.value.loginForm)

            result.onSuccess { data, message ->
                preferencesRepository.saveUser(data.first())
                _sideEffectChannel.send(SideEffect.ShowSuccessToast(message.toString()))
                _uiState.update { it.copy(isLoading = false, isLoggedIn = true) }
            }.onError { error ->
                when (error) {
                    AppError.UNAUTHORIZED -> {
                        preferencesRepository.clearAllTokens()
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