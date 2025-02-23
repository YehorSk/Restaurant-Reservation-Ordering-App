package com.yehorsk.platea.core.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yehorsk.platea.auth.data.repository.AuthRepository
import com.yehorsk.platea.core.data.repository.MainPreferencesRepository
import com.yehorsk.platea.core.data.repository.ProfileRepositoryImpl
import com.yehorsk.platea.core.domain.remote.AppError
import com.yehorsk.platea.core.utils.SideEffect
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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
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

    val userCountryCode: StateFlow<String?> = preferencesRepository.userCountryCodeFlow
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

    init {
        viewModelScope.launch{
            userPhone.collect{ value ->
                val updatedPhone = value ?: ""
                _uiState.update { state ->
                    state.copy(
                        phone = updatedPhone
                    )
                }
            }
        }
        viewModelScope.launch{
            userCountryCode.collect{ value ->
                val updatedCountryCode = value ?: ""
                _uiState.update { state ->
                    state.copy(
                        code = updatedCountryCode
                    )
                }
            }
        }
        viewModelScope.launch{
            userAddress.collect{ value ->
                _uiState.update { state ->
                    state.copy(
                        address = value ?: ""
                    )
                }
            }
        }
        viewModelScope.launch{
            userName.collect{ value ->
                _uiState.update { state ->
                    state.copy(
                        name = value ?: ""
                    )
                }
            }
        }
    }

    fun showDeleteDialog(){
        _uiState.update { currentState ->
            currentState.copy(
                showDeleteAccountDialog = true
            )
        }
    }

    fun hideDeleteDialog(){
        _uiState.update { currentState ->
            currentState.copy(
                showDeleteAccountDialog = false
            )
        }
    }

    fun deleteAccount(){
        viewModelScope.launch{
            _uiState.update { currentState ->
                currentState.copy(
                    isLoading = true
                )
            }
            hideDeleteDialog()
            profileRepositoryImpl.deleteAccount()
                .onSuccess { data,message ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            isLoading = false,
                        )
                    }
                    _sideEffectChannel.send(SideEffect.NavigateToNextScreen)
                }
                .onError { error ->
                    when (error) {
                        AppError.UNAUTHORIZED -> {
                            preferencesRepository.clearAllTokens()
                            _uiState.update { currentState ->
                                currentState.copy(
                                    isLoading = false,
                                    isLoggedOut = true
                                )
                            }
                            _sideEffectChannel.send(SideEffect.NavigateToNextScreen)
                        }

                        else -> {
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
        }
    }

    fun validatePhoneNumber(phone: String){
        val phoneRegex = "^[+]?[0-9]{10,15}$"
        _uiState.update { currentState ->
            currentState.copy(
                isPhoneValid = phone.matches(phoneRegex.toRegex())
            )
        }
    }

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
                    when (error) {
                        AppError.UNAUTHORIZED -> {
                            preferencesRepository.clearAllTokens()
                            _uiState.update { currentState ->
                                currentState.copy(
                                    isLoading = false,
                                    isLoggedOut = true
                                )
                            }
                            _sideEffectChannel.send(SideEffect.NavigateToNextScreen)
                        }

                        else -> {
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
        }
    }

    fun updateTheme(value: Boolean){
        viewModelScope.launch{
            preferencesRepository.setAppTheme(value)
        }
    }

    fun updateName(value: String){
        viewModelScope.launch{
            _uiState.update {
                it.copy(name = value)
            }
        }
    }

    fun updateAddress(value: String){
        viewModelScope.launch{
            _uiState.update {
                it.copy(address = value)
            }
        }
    }

    fun updatePhone(value: String){
        viewModelScope.launch{
            _uiState.update {
                it.copy(phone = value)
            }
        }
    }

    fun updateCode(value: String){
        viewModelScope.launch{
            _uiState.update {
                it.copy(code = value)
            }
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

    fun updateProfile(){
        viewModelScope.launch{
            _uiState.update {
                it.copy(isLoading = true)
            }
            profileRepositoryImpl.updateProfile(_uiState.value.name, _uiState.value.address, _uiState.value.phone, _uiState.value.code)
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