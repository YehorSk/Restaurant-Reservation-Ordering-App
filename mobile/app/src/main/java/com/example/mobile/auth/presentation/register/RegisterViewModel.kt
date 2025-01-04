package com.example.mobile.auth.presentation.register

import androidx.lifecycle.viewModelScope
import com.example.mobile.core.data.repository.MainPreferencesRepository
import com.example.mobile.auth.data.repository.AuthRepository
import com.example.mobile.auth.presentation.BaseAuthViewModel
import com.example.mobile.core.domain.remote.AppError
import com.example.mobile.core.domain.remote.SideEffect
import com.example.mobile.core.domain.remote.onError
import com.example.mobile.core.domain.remote.onSuccess
import com.example.mobile.core.utils.ConnectivityObserver
import com.example.mobile.core.utils.cleanError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    authRepository: AuthRepository,
    preferencesRepository: MainPreferencesRepository,
    networkConnectivityObserver: ConnectivityObserver,
): BaseAuthViewModel(authRepository, preferencesRepository, networkConnectivityObserver) {

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