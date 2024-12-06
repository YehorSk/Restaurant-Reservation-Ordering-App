package com.example.mobile.auth.presentation.login

import androidx.lifecycle.viewModelScope
import com.example.mobile.auth.data.remote.model.AuthResult
import com.example.mobile.core.data.repository.MainPreferencesRepository
import com.example.mobile.auth.data.repository.AuthRepository
import com.example.mobile.auth.presentation.BaseAuthViewModel
import com.example.mobile.core.domain.SideEffect
import com.example.mobile.core.utils.ConnectivityObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
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

    init {
        viewModelScope.launch{
            isNetwork.collect{ available ->
                Timber.tag("NetworkCheck 1").v("Network status: $available")
                if (available == true){
                    Timber.tag("NetworkCheck 2").v("Network status: $available")
                    authenticate()
                }else{
                    Timber.tag("NetworkCheck 3").v("Network status: $available")
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
                Timber.tag("Check ").v(isLoggedIn.toString())
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

    private fun authenticate(){
        Timber.tag("NetworkCheck 2-1").v("authenticate")
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    isLoading = true
                )
            }
            val result = authRepository.authenticate()
            _uiState.update { currentState ->
                Timber.tag("NetworkCheck 2-1").v("result $result")
                when(result){
                    is AuthResult.Authorized -> {
                        Timber.tag("NetworkCheck 2-1").v("Authorized")
                        result.data?.let { preferencesRepository.saveUser(it) }
                        currentState.copy(
                            isLoading = false,
                            isLoggedIn = true
                        )
                    }
                    is AuthResult.Unauthorized -> {
                        Timber.tag("NetworkCheck 2-1").v("Unauthorized")
                        preferencesRepository.clearAllTokens()
                        currentState.copy(
                            isLoading = false,
                            isLoggedIn = false,
                        )
                    }
                    is AuthResult.UnknownError -> {
                        Timber.tag("NetworkCheck 2-1").v("UnknownError")
                        currentState.copy(
                            isLoading = false,
                            isLoggedIn = false
                        )
                    }
                }
            }
        }
    }

    fun login(){
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(isLoading = true)
            }
            val result = authRepository.login(loginForm = uiState.value.loginForm)
            _uiState.update { state ->
                when (result) {
                    is AuthResult.Authorized -> {
                        Timber.tag("Authorized").v(result.data.toString())
                        result.data?.let { preferencesRepository.saveUser(httpResponse = it) }
//                        _sideEffectChannel.send(SideEffect.ShowToast(result.data!!.message!!))
                        state.copy(
                            isLoading = false,
                            isLoggedIn = true
                        )
                    }
                    is AuthResult.Unauthorized -> {
                        Timber.tag("Unauthorized").v(result.data.toString())
                        preferencesRepository.clearAllTokens()
                        _sideEffectChannel.send(SideEffect.ShowToast(result.data!!.message!!))
                        state.copy(
                            isLoading = false,
                            isLoggedIn = false
                        )
                    }
                    is AuthResult.UnknownError -> {
                        Timber.tag("UnknownError").v(result.data.toString())
                        _sideEffectChannel.send(SideEffect.ShowToast(result.data!!.message!!))
                        state.copy(isLoading = false)
                    }
                }
            }
        }
    }
}