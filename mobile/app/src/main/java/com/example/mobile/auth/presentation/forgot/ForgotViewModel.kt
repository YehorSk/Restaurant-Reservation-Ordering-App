package com.example.mobile.auth.presentation.forgot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile.auth.data.repository.AuthRepository
import com.example.mobile.core.data.repository.MainPreferencesRepository
import com.example.mobile.core.domain.remote.AppError
import com.example.mobile.core.domain.remote.SideEffect
import com.example.mobile.core.domain.remote.onError
import com.example.mobile.core.domain.remote.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
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

    private val _sideEffectChannel = Channel<SideEffect>(capacity = Channel.BUFFERED)
    val sideEffectFlow: Flow<SideEffect>
        get() = _sideEffectChannel.receiveAsFlow()

    fun updateforgotUiState(form: ForgotFormState){
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
                _sideEffectChannel.send(SideEffect.ShowSuccessToast(message.toString()))
                _uiState.update { it.copy(isLoading = false) }
            }.onError { error ->
                _sideEffectChannel.send(SideEffect.ShowErrorToast(error))
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

}