package com.yehorsk.platea.core.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yehorsk.platea.auth.domain.repository.AuthRepository
import com.yehorsk.platea.core.data.db.models.RestaurantInfoEntity
import com.yehorsk.platea.core.data.repository.MainPreferencesRepository
import com.yehorsk.platea.core.domain.remote.AppError
import com.yehorsk.platea.core.domain.remote.onError
import com.yehorsk.platea.core.domain.remote.onSuccess
import com.yehorsk.platea.core.domain.repository.ProfileRepository
import com.yehorsk.platea.core.domain.repository.RestaurantRepository
import com.yehorsk.platea.core.utils.SideEffect
import com.yehorsk.platea.core.utils.snackbar.SnackbarController
import com.yehorsk.platea.core.utils.snackbar.SnackbarEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    val authRepository: AuthRepository,
    val profileRepository: ProfileRepository,
    val preferencesRepository: MainPreferencesRepository,
    val restaurantRepository: RestaurantRepository
): ViewModel() {

    val userRole: StateFlow<String?> = preferencesRepository.userRoleFlow
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    val userName: StateFlow<String?> = preferencesRepository.userNameFlow
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

    val restaurantInfoUiState: StateFlow<RestaurantInfoEntity?> = restaurantRepository.getRestaurantInfoFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    protected val _sideEffectChannel = Channel<SideEffect>(capacity = Channel.BUFFERED)
    val sideEffectFlow: Flow<SideEffect>
        get() = _sideEffectChannel.receiveAsFlow()

    init {
        getRestaurantInfo()
        viewModelScope.launch {
            combine(
                userPhone,
                userCountryCode,
                userAddress,
                userName
            ) { phone, code, address, name ->
                SettingsState(
                    phone = phone ?: "",
                    code = code ?: "",
                    address = address ?: "",
                    name = name ?: ""
                )
            }.collect { newState ->
                _uiState.value = _uiState.value.copy(
                    phone = newState.phone,
                    code = newState.code,
                    address = newState.address,
                    name = newState.name
                )
            }
        }
    }

    fun onAction(action: SettingsAction) {
        when (action) {
            is SettingsAction.ShowDeleteDialog -> showDeleteDialog()
            is SettingsAction.HideDeleteDialog -> hideDeleteDialog()
            is SettingsAction.GetRestaurantInfo -> getRestaurantInfo()
            is SettingsAction.DeleteAccount -> deleteAccount()
            is SettingsAction.Logout -> logout()
            is SettingsAction.ValidatePhoneNumber -> validatePhoneNumber(action.phone)
            is SettingsAction.UpdateTheme -> updateTheme(action.value)
            is SettingsAction.UpdateLanguage -> updateLanguage(action.value)
            is SettingsAction.UpdateProfile -> updateProfile(
                action.name,
                action.address,
                action.phone,
                action.code
            )
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

    fun getRestaurantInfo(){
        viewModelScope.launch {
            setLoadingState(true)
            restaurantRepository.getRestaurantInfo()
            setLoadingState(false)
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
            profileRepository.deleteAccount()
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

    fun setLoadingState(isLoading: Boolean) {
        _uiState.update { it.copy(isLoading = isLoading) }
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

    fun updateLanguage(value: String){
        viewModelScope.launch{
            _uiState.update {
                it.copy(isLoading = true)
            }
            preferencesRepository.setAppLanguage(value)
            authRepository.setLocale(value)
                .onSuccess { data,message ->
                    _sideEffectChannel.send(SideEffect.LanguageChanged)
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

    fun updateProfile(name: String, address: String, phone: String, code: String){
        viewModelScope.launch{
            _uiState.update {
                it.copy(isLoading = true)
            }
            profileRepository.updateProfile(name, address, phone, code)
                .onSuccess { data,message ->
                    _uiState.update {
                        it.copy(
                            name = name,
                            address = address,
                            phone = phone,
                            code = code,
                            isLoading = false
                        )
                    }
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
                    _uiState.update {
                        it.copy(isLoading = false)
                    }
                }

        }
    }
}