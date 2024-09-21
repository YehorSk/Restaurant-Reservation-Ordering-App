package com.example.mobile.ui.screens.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile.auth.data.model.AuthResult
import com.example.mobile.auth.data.model.HttpResponse
import com.example.mobile.auth.data.repository.AuthPreferencesRepository
import com.example.mobile.auth.data.repository.AuthRepository
import com.example.mobile.utils.ConnectivityRepository
import com.example.mobile.utils.cleanError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    val authRepository: AuthRepository,
    val preferencesRepository: AuthPreferencesRepository,
    val connectivityRepository: ConnectivityRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(LoginState())
    val uiState: StateFlow<LoginState> = _uiState.asStateFlow()

    val userRole: StateFlow<String?> = preferencesRepository.userRoleFlow
        .stateIn(viewModelScope, SharingStarted.Lazily, null)


    init {
        val isOnline = connectivityRepository.isInternetConnected()
        if (isOnline){
            authenticate()
            _uiState.update { currentState ->
                currentState.copy(internetError = false)
            }
        }else{
            checkIfLoggedIn()
            _uiState.update { currentState ->
                currentState.copy(internetError = true)
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
                Timber.tag("Check ").v("Token received: $token")
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
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    isLoading = true
                )
            }
            val result = authRepository.authenticate()
            _uiState.update { currentState ->
                when(result){
                    is AuthResult.Authorized -> {
                        result.data?.let { preferencesRepository.saveUser(it) }
                        currentState.copy(
                            isLoading = false,
                            isLoggedIn = true,
                            loginFormErrors = LoginFormErrors(
                                email = "",
                                password = ""
                            )
                        )
                    }
                    is AuthResult.Unauthorized -> {
                        preferencesRepository.clearAllTokens()
                        currentState.copy(
                            isLoading = false,
                            isLoggedIn = false,
                        )
                    }
                    is AuthResult.UnknownError -> {
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
                        state.copy(
                            isLoading = false,
                            loginFormErrors = LoginFormErrors(
                                email = "",
                                password = ""
                            ),
                            isLoggedIn = true
                        )
                    }
                    is AuthResult.Unauthorized -> {
                        Timber.tag("Unauthorized").v(result.data.toString())
                        preferencesRepository.clearAllTokens()
                        state.copy(
                            isLoading = false,
                            loginFormErrors = LoginFormErrors(
                                email = cleanError(result.data?.errors?.email.toString()),
                                password = cleanError(result.data?.errors?.password.toString())
                            ),
                            isLoggedIn = false
                        )
                    }
                    is AuthResult.UnknownError -> {
                        Timber.tag("UnknownError").v(result.data.toString())
                        state.copy(isLoading = false)
                    }
                }
            }
        }
    }
}