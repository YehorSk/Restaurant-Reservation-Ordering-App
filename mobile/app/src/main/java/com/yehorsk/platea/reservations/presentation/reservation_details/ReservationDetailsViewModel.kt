package com.yehorsk.platea.reservations.presentation.reservation_details

import androidx.lifecycle.viewModelScope
import com.yehorsk.platea.core.data.repository.MainPreferencesRepository
import com.yehorsk.platea.core.domain.remote.SideEffect
import com.yehorsk.platea.core.domain.remote.onError
import com.yehorsk.platea.core.domain.remote.onSuccess
import com.yehorsk.platea.core.utils.ConnectivityObserver
import com.yehorsk.platea.reservations.data.dao.ReservationDao
import com.yehorsk.platea.reservations.data.db.model.ReservationEntity
import com.yehorsk.platea.reservations.data.remote.ReservationRepositoryImpl
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
    reservationRepositoryImpl: ReservationRepositoryImpl,
    reservationDao: ReservationDao,
    preferencesRepository: MainPreferencesRepository
): ReservationBaseViewModel(networkConnectivityObserver, reservationRepositoryImpl, reservationDao, preferencesRepository){

    val reservationItemUiState: StateFlow<List<ReservationEntity>> = reservationDao.getUserReservations()
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
            reservationRepositoryImpl.getUserReservationDetails(id)
                .onError { error ->
                    _sideEffectChannel.send(SideEffect.ShowErrorToast(error))
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
            reservationRepositoryImpl.cancelUserReservation(id)
                .onSuccess { data, message ->
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

    fun updateReservation(id: String, status: Status) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true)
            }
            reservationRepositoryImpl.updateReservation(id, status)
                .onSuccess { data, message ->
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
