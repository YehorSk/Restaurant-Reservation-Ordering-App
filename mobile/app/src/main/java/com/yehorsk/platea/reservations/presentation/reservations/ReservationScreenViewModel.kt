package com.yehorsk.platea.reservations.presentation.reservations

import androidx.lifecycle.viewModelScope
import com.yehorsk.platea.core.data.repository.MainPreferencesRepository
import com.yehorsk.platea.core.domain.remote.ReservationFilter
import com.yehorsk.platea.core.domain.remote.onError
import com.yehorsk.platea.core.domain.repository.RestaurantRepository
import com.yehorsk.platea.core.utils.ConnectivityObserver
import com.yehorsk.platea.core.utils.snackbar.SnackbarController
import com.yehorsk.platea.core.utils.snackbar.SnackbarEvent
import com.yehorsk.platea.reservations.data.db.model.ReservationEntity
import com.yehorsk.platea.reservations.domain.repository.ReservationRepository
import com.yehorsk.platea.reservations.presentation.ReservationBaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flattenMerge
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ReservationScreenViewModel @Inject constructor(
    networkConnectivityObserver: ConnectivityObserver,
    reservationRepository: ReservationRepository,
    preferencesRepository: MainPreferencesRepository,
    restaurantRepository: RestaurantRepository
): ReservationBaseViewModel(networkConnectivityObserver, reservationRepository, preferencesRepository, restaurantRepository){

    private val _filterOption = MutableStateFlow(ReservationFilter.ALL)
    val filterOption= _filterOption.asStateFlow()

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val reservationItemUiState: StateFlow<List<ReservationEntity>> = combine(_filterOption, _searchText) { filter, search ->
        when (filter) {
            ReservationFilter.PENDING -> reservationRepository.getUserReservationsFlow(search, "Pending")
            ReservationFilter.CANCELLED -> reservationRepository.getUserReservationsFlow(search, "Cancelled")
            ReservationFilter.CONFIRMED -> reservationRepository.getUserReservationsFlow(search, "Confirmed")
            else -> reservationRepository.getUserReservationsFlow(search)
        }
    }.flattenMerge()
        .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = listOf()
    )

    init {
        getReservations()
    }

    fun onAction(action: ReservationScreenAction){
        when(action){
            ReservationScreenAction.GetReservations -> getReservations()
            is ReservationScreenAction.OnSearchValueChange -> onSearchValueChange(action.value)
            is ReservationScreenAction.UpdateFilter -> updateFilter(action.filter)
        }
    }

    fun onSearchValueChange(value: String){
        _searchText.update { value }
    }

    fun updateFilter(option: ReservationFilter) {
        _filterOption.value = option
    }

    fun getReservations(){
        Timber.d("getReservations")
        viewModelScope.launch{
            _uiState.update { it.copy(isLoading = true) }
            reservationRepository.getUserReservations()
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