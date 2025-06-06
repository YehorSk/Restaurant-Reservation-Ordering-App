package com.yehorsk.platea.reservations.presentation.reservation_details

import androidx.lifecycle.viewModelScope
import com.yehorsk.platea.core.data.dao.RestaurantInfoDao
import com.yehorsk.platea.core.data.repository.MainPreferencesRepository
import com.yehorsk.platea.core.domain.remote.onError
import com.yehorsk.platea.core.domain.remote.onSuccess
import com.yehorsk.platea.core.domain.repository.RestaurantRepository
import com.yehorsk.platea.core.utils.ConnectivityObserver
import com.yehorsk.platea.core.utils.snackbar.SnackbarController
import com.yehorsk.platea.core.utils.snackbar.SnackbarEvent
import com.yehorsk.platea.reservations.data.dao.ReservationDao
import com.yehorsk.platea.reservations.data.db.model.ReservationEntity
import com.yehorsk.platea.reservations.data.remote.ReservationRepositoryImpl
import com.yehorsk.platea.reservations.domain.models.Reservation
import com.yehorsk.platea.reservations.domain.repository.ReservationRepository
import com.yehorsk.platea.reservations.presentation.ReservationBaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReservationDetailsViewModel @Inject constructor(
    networkConnectivityObserver: ConnectivityObserver,
    reservationRepository: ReservationRepository,
    preferencesRepository: MainPreferencesRepository,
    restaurantRepository: RestaurantRepository
): ReservationBaseViewModel(networkConnectivityObserver, reservationRepository, preferencesRepository, restaurantRepository){

    val reservationItemUiState: StateFlow<List<Reservation>> = reservationRepository.getUserReservationsFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = listOf()
        )

    fun onAction(action: ReservationDetailsAction){
        when(action){
            is ReservationDetailsAction.CancelReservation -> cancelReservation(action.id)
            is ReservationDetailsAction.GetReservationDetails -> getReservationDetails(action.id)
            is ReservationDetailsAction.SetCancelledStatus -> updateReservation(action.id, action.status)
            is ReservationDetailsAction.SetConfirmedStatus -> updateReservation(action.id, action.status)
            is ReservationDetailsAction.SetPendingStatus -> updateReservation(action.id, action.status)
        }
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
