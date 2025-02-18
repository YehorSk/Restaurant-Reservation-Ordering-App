package com.yehorsk.platea.reservations.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yehorsk.platea.core.data.repository.MainPreferencesRepository
import com.yehorsk.platea.core.utils.SideEffect
import com.yehorsk.platea.core.utils.ConnectivityObserver
import com.yehorsk.platea.reservations.data.dao.ReservationDao
import com.yehorsk.platea.reservations.data.remote.ReservationRepositoryImpl
import com.yehorsk.platea.reservations.presentation.reservations.ReservationForm
import com.yehorsk.platea.reservations.presentation.reservations.ReservationUiState
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
open class ReservationBaseViewModel @Inject constructor(
    val networkConnectivityObserver: ConnectivityObserver,
    val reservationRepositoryImpl: ReservationRepositoryImpl,
    val reservationDao: ReservationDao,
    val preferencesRepository: MainPreferencesRepository,
): ViewModel(){

    val userRole: StateFlow<String?> = preferencesRepository.userRoleFlow
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    val userPhone: StateFlow<String?> = preferencesRepository.userPhoneFlow
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    val userCountryCode: StateFlow<String?> = preferencesRepository.userCountryCodeFlow
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    protected val _uiState = MutableStateFlow(ReservationUiState())
    val uiState = _uiState.asStateFlow()

    val isNetwork = MutableStateFlow<Boolean>(networkConnectivityObserver.isAvailable)

    protected val _sideEffectChannel = Channel<SideEffect>(capacity = Channel.BUFFERED)
    val sideEffectFlow: Flow<SideEffect>
        get() = _sideEffectChannel.receiveAsFlow()

    init {
        viewModelScope.launch{
            networkConnectivityObserver.observe().collect { status ->
                isNetwork.value = status
            }
        }
        viewModelScope.launch {
            userPhone.collect { phone ->
                _uiState.update { state ->
                    val updatedPhone = phone ?: ""
                    val updatedFullPhone = "${state.reservationForm.countryCode}${updatedPhone}"
                    state.copy(reservationForm = state.reservationForm.copy(phone = updatedPhone, fullPhone = updatedFullPhone))
                }
            }
        }
        viewModelScope.launch {
            userCountryCode.collect { code ->
                _uiState.update { state ->
                    val updatedCountryCode = code ?: ""
                    val updatedFullPhone = "${updatedCountryCode}${state.reservationForm.phone}"
                    state.copy(reservationForm = state.reservationForm.copy(countryCode = updatedCountryCode, fullPhone = updatedFullPhone))
                }
            }
        }
    }

    protected fun setLoadingState(isLoading: Boolean) {
        _uiState.update { it.copy(isLoading = isLoading) }
    }

    fun clearForm(){
        _uiState.update { it.copy(reservationForm = ReservationForm()) }
    }
}