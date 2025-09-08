package com.yehorsk.platea.reservations.presentation.reservation_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.yehorsk.platea.core.data.repository.MainPreferencesRepository
import com.yehorsk.platea.core.domain.remote.onError
import com.yehorsk.platea.core.domain.remote.onSuccess
import com.yehorsk.platea.core.navigation.Screen
import com.yehorsk.platea.core.utils.ConnectivityObserver
import com.yehorsk.platea.core.utils.snackbar.SnackbarController
import com.yehorsk.platea.core.utils.snackbar.SnackbarEvent
import com.yehorsk.platea.reservations.domain.repository.ReservationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReservationDetailsViewModel @Inject constructor(
    private val networkConnectivityObserver: ConnectivityObserver,
    private val reservationRepository: ReservationRepository,
    private val preferencesRepository: MainPreferencesRepository,
    savedStateHandle: SavedStateHandle
): ViewModel(){

    private var reservationDetailsJob: Job? = null

    private var reservationId = savedStateHandle.toRoute<Screen.ReservationDetails>().id

    private val _uiState = MutableStateFlow(ReservationDetailsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        observeReservationDetails()
        observeNetwork()
        observeUserRole()
    }

    fun onAction(action: ReservationDetailsAction){
        when(action){
            is ReservationDetailsAction.CancelReservation -> cancelReservation(action.id)
            is ReservationDetailsAction.GetReservationDetails -> getReservationDetails(action.id)
            is ReservationDetailsAction.SetCancelledStatus -> updateReservation(action.id, action.status)
            is ReservationDetailsAction.SetConfirmedStatus -> updateReservation(action.id, action.status)
            is ReservationDetailsAction.SetPendingStatus -> updateReservation(action.id, action.status)
        }
    }

    private fun observeReservationDetails(){
        reservationDetailsJob?.cancel()
        reservationDetailsJob = reservationRepository.getUserReservationDetailsFlow(reservationId.toString())
            .onEach { reservation ->
                _uiState.update { it.copy(
                    currentReservation = reservation
                ) }
            }
            .launchIn(viewModelScope)
    }

    private fun observeUserRole(){
        preferencesRepository.userRoleFlow
            .onEach { userRole ->
                _uiState.update { it.copy(
                    userRole = userRole
                ) }
            }
            .launchIn(viewModelScope)
    }

    private fun observeNetwork(){
        networkConnectivityObserver.observe()
            .onEach { network ->
                _uiState.update { it.copy(
                    isNetwork = network
                ) }
            }
            .launchIn(viewModelScope)
    }

    fun getReservationDetails(id: String){
        viewModelScope.launch{
            _uiState.update {
                it.copy(isLoading = true)
            }
            reservationRepository.getUserReservationDetails(id)
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

    fun cancelReservation(id: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true)
            }
            reservationRepository.cancelUserReservation(id)
                .onSuccess { data, message ->
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

    fun updateReservation(id: String, status: Status) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true)
            }
            reservationRepository.updateReservation(id, status)
                .onSuccess { data, message ->
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
