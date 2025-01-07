package com.example.mobile.core.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile.core.data.repository.MainPreferencesRepository
import com.example.mobile.auth.data.repository.AuthRepository
import com.example.mobile.core.data.repository.ProfileRepositoryImpl
import com.example.mobile.core.domain.remote.SideEffect
import com.example.mobile.core.domain.remote.onError
import com.example.mobile.core.domain.remote.onSuccess
import com.example.mobile.core.domain.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    val authRepository: AuthRepository,
    val profileRepositoryImpl: ProfileRepositoryImpl,
    val preferencesRepository: MainPreferencesRepository
): ViewModel() {

    val userRole: StateFlow<String?> = preferencesRepository.userRoleFlow
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    val userName: StateFlow<String?> = preferencesRepository.userNameFlow
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    val userEmail: StateFlow<String?> = preferencesRepository.userEmailFlow
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    val userAddress: StateFlow<String?> = preferencesRepository.userAddressFlow
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    val userPhone: StateFlow<String?> = preferencesRepository.userPhoneFlow
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    val _uiState = MutableStateFlow(SettingsState())
    val uiState: StateFlow<SettingsState> = _uiState.asStateFlow()

    protected val _sideEffectChannel = Channel<SideEffect>(capacity = Channel.BUFFERED)
    val sideEffectFlow: Flow<SideEffect>
        get() = _sideEffectChannel.receiveAsFlow()

    fun logout(){
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    isLoading = true
                )
            }
            authRepository.logout()
            preferencesRepository.clearAllTokens()
            _uiState.update { currentState ->
                currentState.copy(
                    isLoading = false,
                    isLoggedOut = true
                )
            }

        }
    }

    fun updateProfile(
        name: String,
        address: String,
        phone: String
    ){
        viewModelScope.launch{
            _uiState.update {
                it.copy(isLoading = true)
            }
            profileRepositoryImpl.updateProfile(name,address,phone)
                .onSuccess { data,message ->
                    _sideEffectChannel.send(SideEffect.ShowSuccessToast(message.toString()))
                }
                .onError { error ->
                    _sideEffectChannel.send(SideEffect.ShowErrorToast(error))
                }
            _uiState.update {
                it.copy(isLoading = false)
            }
        }
    }
}