package com.yehorsk.platea.reservations.presentation.reservations

import androidx.lifecycle.viewModelScope
import com.yehorsk.platea.core.data.repository.MainPreferencesRepository
import com.yehorsk.platea.core.domain.remote.ReservationFilter
import com.yehorsk.platea.core.domain.remote.onError
import com.yehorsk.platea.core.domain.remote.onSuccess
import com.yehorsk.platea.core.utils.ConnectivityObserver
import com.yehorsk.platea.core.utils.snackbar.SnackbarController
import com.yehorsk.platea.core.utils.snackbar.SnackbarEvent
import com.yehorsk.platea.reservations.data.dao.ReservationDao
import com.yehorsk.platea.reservations.data.db.model.ReservationEntity
import com.yehorsk.platea.reservations.data.remote.ReservationRepositoryImpl
import com.yehorsk.platea.reservations.data.remote.dto.toReservationEntity
import com.yehorsk.platea.reservations.presentation.ReservationBaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ReservationScreenViewModel @Inject constructor(
    networkConnectivityObserver: ConnectivityObserver,
    reservationRepositoryImpl: ReservationRepositoryImpl,
    reservationDao: ReservationDao,
    preferencesRepository: MainPreferencesRepository
): ReservationBaseViewModel(networkConnectivityObserver, reservationRepositoryImpl, reservationDao, preferencesRepository){

    private val _filterOption = MutableStateFlow(ReservationFilter.ALL)
    val filterOption= _filterOption.asStateFlow()

    val reservationItemUiState: StateFlow<List<ReservationEntity>> = reservationDao.getUserReservations()
        .combine(_filterOption){ reservations, filter ->
            when(filter){
                ReservationFilter.PENDING -> reservations.filter { it.status == "Pending" }
                ReservationFilter.CANCELLED -> reservations.filter { it.status == "Cancelled" }
                ReservationFilter.CONFIRMED -> reservations.filter { it.status == "Confirmed" }
                else -> reservations
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = listOf()
        )

    init {
        getReservations()
    }

    fun updateFilter(option: ReservationFilter) {
        _filterOption.value = option
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
                    SnackbarController.sendEvent(
                        event = SnackbarEvent(
                            error = error
                        )
                    )
                }
            _uiState.update { it.copy(isLoading = false) }
        }
    }

}