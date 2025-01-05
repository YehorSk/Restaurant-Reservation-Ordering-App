package com.example.mobile.reservations.presentation.reservation_details

import androidx.lifecycle.viewModelScope
import com.example.mobile.core.domain.remote.ReservationFilter
import com.example.mobile.core.domain.remote.SideEffect
import com.example.mobile.core.domain.remote.onError
import com.example.mobile.core.domain.remote.onSuccess
import com.example.mobile.core.utils.ConnectivityObserver
import com.example.mobile.reservations.data.dao.ReservationDao
import com.example.mobile.reservations.data.db.model.ReservationEntity
import com.example.mobile.reservations.data.remote.ReservationRepositoryImpl
import com.example.mobile.reservations.presentation.ReservationBaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReservationDetailsViewModel @Inject constructor(
    networkConnectivityObserver: ConnectivityObserver,
    reservationRepositoryImpl: ReservationRepositoryImpl,
    reservationDao: ReservationDao
): ReservationBaseViewModel(networkConnectivityObserver, reservationRepositoryImpl, reservationDao){

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

    fun cancelReservation(id: String){
        viewModelScope.launch{
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
}
