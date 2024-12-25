package com.example.mobile.orders.presentation.reservations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile.core.domain.SideEffect
import com.example.mobile.core.domain.onError
import com.example.mobile.core.domain.onSuccess
import com.example.mobile.core.utils.ConnectivityObserver
import com.example.mobile.orders.data.dao.OrderDao
import com.example.mobile.orders.data.dao.ReservationDao
import com.example.mobile.orders.data.db.model.ReservationEntity
import com.example.mobile.orders.data.remote.OrderRepositoryImpl
import com.example.mobile.orders.data.remote.dto.toReservationEntity
import com.example.mobile.orders.presentation.OrderBaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ReservationScreenViewModel @Inject constructor(
    networkConnectivityObserver: ConnectivityObserver,
    orderRepositoryImpl: OrderRepositoryImpl,
    orderDao: OrderDao,
    val reservationDao: ReservationDao
): OrderBaseViewModel(networkConnectivityObserver, orderRepositoryImpl, orderDao){

    val reservationItemUiState: StateFlow<List<ReservationEntity>> = reservationDao.getUserReservations()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = listOf()
        )

    init {
        getReservations()
    }

    fun getReservations(){
        Timber.d("getReservations")
        viewModelScope.launch{
            _uiState.update { it.copy(isLoading = true) }
            val localItems = reservationItemUiState.value

            orderRepositoryImpl.getUserReservations()
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