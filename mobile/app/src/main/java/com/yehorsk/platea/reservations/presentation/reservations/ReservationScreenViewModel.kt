package com.yehorsk.platea.reservations.presentation.reservations

import androidx.lifecycle.viewModelScope
import com.yehorsk.platea.R
import com.yehorsk.platea.core.data.repository.MainPreferencesRepository
import com.yehorsk.platea.core.domain.remote.ReservationFilter
import com.yehorsk.platea.core.domain.remote.onError
import com.yehorsk.platea.core.domain.repository.RestaurantRepository
import com.yehorsk.platea.core.utils.ConnectivityObserver
import com.yehorsk.platea.core.utils.UiText
import com.yehorsk.platea.core.utils.snackbar.SnackbarController
import com.yehorsk.platea.core.utils.snackbar.SnackbarEvent
import com.yehorsk.platea.orders.domain.models.Order
import com.yehorsk.platea.reservations.domain.models.Reservation
import com.yehorsk.platea.reservations.domain.repository.ReservationRepository
import com.yehorsk.platea.reservations.presentation.ReservationBaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flattenMerge
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class ReservationScreenViewModel @Inject constructor(
    networkConnectivityObserver: ConnectivityObserver,
    reservationRepository: ReservationRepository,
    preferencesRepository: MainPreferencesRepository,
    restaurantRepository: RestaurantRepository
): ReservationBaseViewModel(networkConnectivityObserver, reservationRepository, preferencesRepository, restaurantRepository){

    private var reservationJob: Job? = null
    private var cachedReservation = emptyList<Reservation>()

    private val _filterOption = MutableStateFlow(ReservationFilter.ALL)
    val filterOption= _filterOption.asStateFlow()

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    init {
        getReservations()
        if(cachedReservation.isEmpty()){
            observeReservations()
        }
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

    fun Flow<List<Reservation>>.groupByDate(): Flow<Map<UiText,List<Reservation>>>{
        val formatter = DateTimeFormatter.ofPattern("dd MMMM")
        val today = LocalDate.now()
        return map { reservations ->
            reservations
                .groupBy { reservation ->
                    Instant.parse(reservation.createdAt)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                }
                .mapValues { (_, reservations) ->
                    reservations.sortedBy { r ->
                        Instant.parse(r.createdAt)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                    }
                }
                .toSortedMap(compareByDescending { it })
                .mapKeys { (date, _) ->
                    when(date){
                        today -> UiText.StringResource(R.string.today)
                        today.plusDays(1) -> UiText.StringResource(R.string.tomorrow)
                        today.minusDays(1) -> UiText.StringResource(R.string.yesterday)
                        else -> UiText.DynamicString(date!!.format(formatter))
                    }
                }
        }
    }

    private fun observeReservations() {
        reservationJob?.cancel()
        reservationJob = reservationRepository
            .getUserReservationsFlow()
            .groupByFilterAndSearch()
            .groupByDate()
            .onEach { reservations ->
                _uiState.update { it.copy(
                    reservations = reservations
                ) }
            }
            .flowOn(Dispatchers.Default)
            .launchIn(viewModelScope)
    }

    fun Flow<List<Reservation>>.groupByFilterAndSearch(): Flow<List<Reservation>>{
        return combine(
            this,
            _searchText,
            _filterOption
        ) { orders, search, filterOption ->
            orders.filter { reservation ->
                val matchesStatus = filterOption.status
                    ?.let { it == reservation.status }
                    ?: true

                val matchesSearch = search.isBlank() ||
                        reservation.code.contains(search, ignoreCase = true)

                matchesStatus && matchesSearch
            }
        }
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