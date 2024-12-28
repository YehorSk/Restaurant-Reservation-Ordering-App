package com.example.mobile.auth.presentation.register

import androidx.lifecycle.viewModelScope
import com.example.mobile.auth.data.remote.model.AuthResult
import com.example.mobile.core.data.repository.MainPreferencesRepository
import com.example.mobile.auth.data.repository.AuthRepository
import com.example.mobile.auth.presentation.BaseAuthViewModel
import com.example.mobile.core.domain.remote.SideEffect
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

    init {
        viewModelScope.launch{
            isNetwork.collect{ available ->
                if (!available) {
                    _sideEffectChannel.send(SideEffect.ShowSuccessToast("No internet connection!"))
                }
            }
        }
    }

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

    fun register(){
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    isLoading = true
                )
            }
            val result = authRepository.register(registerForm = uiState.value.registerForm)
            _uiState.update { currentState ->
                when (result) {
                    is AuthResult.Authorized -> {
                        Timber.tag("Authorized").v(result.data.toString())
                        result.data?.let { preferencesRepository.saveUser(it) }
                        currentState.copy(
                            isLoading = false,
                            isLoggedIn = true,
                            registerFormErrors = RegisterFormErrors(
                                email = "",
                                password = "",
                                passwordConfirm = "",
                                name = "",
                            )
                        )
                    }

                    is AuthResult.Unauthorized -> {
                        Timber.tag("Unauthorized").v(result.data.toString())
                        currentState.copy(
                            isLoggedIn = false,
                            isLoading = false,
                            registerFormErrors = RegisterFormErrors(
                                email = cleanError(result.data?.errors?.email.toString()),
                                password = cleanError(result.data?.errors?.password.toString()),
                                passwordConfirm = cleanError(result.data?.errors?.passwordConfirmation.toString()),
                                name = cleanError(result.data?.errors?.name.toString()),
                            )
                        )
                    }

                    is AuthResult.UnknownError -> {
                        Timber.tag("UnknownError").v(result.data.toString())
                        currentState.copy(isLoading = false)
                    }
                }
            }
        }
    }

}