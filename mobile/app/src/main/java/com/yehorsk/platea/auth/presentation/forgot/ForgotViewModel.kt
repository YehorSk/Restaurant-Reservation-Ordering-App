package com.yehorsk.platea.auth.presentation.forgot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yehorsk.platea.auth.domain.repository.AuthRepository
import com.yehorsk.platea.core.data.repository.MainPreferencesRepository
import com.yehorsk.platea.core.domain.remote.AppError
import com.yehorsk.platea.core.domain.remote.onError
import com.yehorsk.platea.core.domain.remote.onSuccess
import com.yehorsk.platea.core.utils.snackbar.SnackbarController
import com.yehorsk.platea.core.utils.snackbar.SnackbarEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotViewModel @Inject constructor(
    val authRepository: AuthRepository,
    val preferencesRepository: MainPreferencesRepository,
): ViewModel() {

    private val _uiState = MutableStateFlow(ForgotState())
    val uiState: StateFlow<ForgotState> = _uiState.asStateFlow()

    fun updateForgotUiState(form: ForgotFormState){
        _uiState.update { state ->
            state.copy(
                form = form,
                isEntryValid = validateLogInput(state)
            )
        }
    }

    private fun validateLogInput(uiState: ForgotState): Boolean{
        return with(uiState.form){
            email.isNotBlank()
        }
    }

    fun forgotPassword() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val result = authRepository.forgotPassword(forgotFormState = uiState.value.form)

            result.onSuccess { data, message ->
                SnackbarController.sendEvent(
                    event = SnackbarEvent(
                        message = message
                    )
                )
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        form = ForgotFormState()
                    )
                }
            }.onError { error ->
                when(error){
                    is AppError.IncorrectData -> {
                        SnackbarController.sendEvent(
                            event = SnackbarEvent(
                                error = error
                            )
                        )
                        _uiState.update { it.copy(isLoading = false) }
                    }
                    else -> {
                        SnackbarController.sendEvent(
                            event = SnackbarEvent(
                                error = error
                            )
                        )
                        _uiState.update { it.copy(isLoading = false) }
                    }
                }
            }
        }
    }

}