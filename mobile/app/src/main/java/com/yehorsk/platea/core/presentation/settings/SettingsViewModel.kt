package com.yehorsk.platea.core.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yehorsk.platea.auth.data.repository.AuthRepository
import com.yehorsk.platea.core.data.repository.MainPreferencesRepository
import com.yehorsk.platea.core.data.repository.ProfileRepositoryImpl
import com.yehorsk.platea.core.domain.remote.SideEffect
import com.yehorsk.platea.core.domain.remote.onError
import com.yehorsk.platea.core.domain.remote.onSuccess
import com.yehorsk.platea.core.utils.snackbar.SnackbarController
import com.yehorsk.platea.core.utils.snackbar.SnackbarEvent
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

    val userTheme: StateFlow<Boolean?> = preferencesRepository.appIsDarkThemeFlow
        .stateIn(viewModelScope, SharingStarted.Lazily, false)

    val userLang: StateFlow<String?> = preferencesRepository.appLanguageFlow
        .stateIn(viewModelScope, SharingStarted.Lazily, "en")

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
                .onSuccess { data,message ->
                    SnackbarController.sendEvent(
                        event = SnackbarEvent(
                            message = message.toString()
                        )
                    )
                    preferencesRepository.clearAllTokens()
                    _uiState.update { currentState ->
                        currentState.copy(
                            isLoading = false,
                            isLoggedOut = true
                        )
                    }
                    _sideEffectChannel.send(SideEffect.NavigateToNextScreen)
                }
                .onError { error ->
                                        SnackbarController.sendEvent(
                        event = SnackbarEvent(
                            error = error
                        )
                    )
                    _uiState.update { currentState ->
                        currentState.copy(
                            isLoading = false,
                        )
                    }
                }
        }
    }

    fun updateTheme(value: Boolean){
        viewModelScope.launch{
            preferencesRepository.setAppTheme(value)
        }
    }

    fun updateLanguage(value: String){
        viewModelScope.launch{
            _uiState.update {
                it.copy(isLoading = true)
            }
            preferencesRepository.setAppLanguage(value)
            authRepository.setLocale(value)
                .onSuccess { data,message ->
                    SnackbarController.sendEvent(
                        event = SnackbarEvent(
                            message = message.toString()
                        )
                    )
                }
                .onError { error ->
                    SnackbarController.sendEvent(
                        event = SnackbarEvent(
                            error = error
                        )
                    )
                }
            _uiState.update {
                it.copy(isLoading = false)
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
                    SnackbarController.sendEvent(
                        event = SnackbarEvent(
                            message = message.toString()
                        )
                    )
                }
                .onError { error ->
                    SnackbarController.sendEvent(
                        event = SnackbarEvent(
                            error = error
                        )
                    )
                }
            _uiState.update {
                it.copy(isLoading = false)
            }
        }
    }
}