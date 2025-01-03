package com.example.mobile.reservations.presentation.reservations

import androidx.lifecycle.viewModelScope
import com.example.mobile.core.domain.remote.SideEffect
import com.example.mobile.core.domain.remote.onError
import com.example.mobile.core.domain.remote.onSuccess
import com.example.mobile.core.utils.ConnectivityObserver
import com.example.mobile.reservations.data.dao.ReservationDao
import com.example.mobile.reservations.data.remote.dto.toReservationEntity
import com.example.mobile.reservations.data.remote.ReservationRepositoryImpl
import com.example.mobile.reservations.presentation.ReservationBaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ReservationScreenViewModel @Inject constructor(
    networkConnectivityObserver: ConnectivityObserver,
    reservationRepositoryImpl: ReservationRepositoryImpl,
    reservationDao: ReservationDao
): ReservationBaseViewModel(networkConnectivityObserver, reservationRepositoryImpl, reservationDao){

    init {
        getReservations()
    }

    fun getReservations(){
        Timber.d("getReservations")
        viewModelScope.launch{
            _uiState.update { it.copy(isLoading = true) }
            val localItems = reservationItemUiState.value

            reservationRepositoryImpl.getUserReservations()
                .onSuccess { data, message ->
                    val serverItemIds = data.map { it.id }.toSet()
                    val itemsToDelete = localItems.filter { it.id !in serverItemIds }

                    reservationDao.runInTransaction {
                        reservationDao.insertReservations(data.map { it.toReservationEntity() })
                        reservationDao.deleteItems(itemsToDelete)
                    }
                }
                .onError { error ->
                    _sideEffectChannel.send(SideEffect.ShowErrorToast(error))
                }
            _uiState.update { it.copy(isLoading = false) }
        }
    }

}