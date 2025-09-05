package com.yehorsk.platea.orders.presentation.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yehorsk.platea.R
import com.yehorsk.platea.core.data.repository.MainPreferencesRepository
import com.yehorsk.platea.core.domain.remote.OrderFilter
import com.yehorsk.platea.core.domain.remote.onError
import com.yehorsk.platea.core.utils.ConnectivityObserver
import com.yehorsk.platea.core.utils.UiText
import com.yehorsk.platea.core.utils.snackbar.SnackbarController
import com.yehorsk.platea.core.utils.snackbar.SnackbarEvent
import com.yehorsk.platea.orders.domain.models.Order
import com.yehorsk.platea.orders.domain.repository.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(
    private val networkConnectivityObserver: ConnectivityObserver,
    private val orderRepository: OrderRepository,
    val preferencesRepository: MainPreferencesRepository,
    ): ViewModel(){

    private var ordersJob: Job? = null
    private var cachedOrders = emptyList<Order>()

    private val _filterOption = MutableStateFlow(OrderFilter.ALL)
    val filterOption= _filterOption.asStateFlow()

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _uiState = MutableStateFlow(OrdersUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getUserOrders()
        if(cachedOrders.isEmpty()){
            observeOrders()
        }
        observeUserRole()
    }

    fun onAction(action: OrdersAction){
        when(action){
            OrdersAction.GetUserOrders -> getUserOrders()
            is OrdersAction.UpdateFilter -> updateFilter(action.filter)
            is OrdersAction.UpdateSearch -> onSearchValueChange(action.search)
        }
    }

    private fun observeUserRole(){
        preferencesRepository.userRoleFlow
            .onEach { role ->
                _uiState.update { it.copy(
                    userRole = role
                ) }
            }
            .launchIn(viewModelScope)
    }

    fun Flow<List<Order>>.groupByDate(): Flow<Map<UiText,List<Order>>>{
        val formatter = DateTimeFormatter.ofPattern("dd MMMM")
        val today = LocalDate.now()
        return map { orders ->
            orders
                .groupBy { order ->
                    Instant.parse(order.createdAt)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                }
                .mapValues { (_, orders) ->
                    orders.sortedBy { o ->
                        Instant.parse(o.createdAt)
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

    fun Flow<List<Order>>.groupByFilterAndSearch(): Flow<List<Order>>{
        return combine(
            this,
            _searchText,
            _filterOption
        ) { orders, search, filterOption ->
            orders.filter { order ->
                val matchesStatus = filterOption.status
                    ?.let { it == order.status }
                    ?: true

                val matchesSearch = search.isBlank() ||
                        order.code.contains(search, ignoreCase = true)

                matchesStatus && matchesSearch
            }
        }
    }

    private fun observeOrders(){
        ordersJob?.cancel()
        ordersJob = orderRepository
            .getUserOrdersFlow()
            .groupByFilterAndSearch()
            .groupByDate()
            .onEach { orders ->
                _uiState.update { it.copy(
                    orders = orders
                ) }
            }
            .flowOn(Dispatchers.Default)
            .launchIn(viewModelScope)
    }

    private fun onSearchValueChange(value: String){
        _searchText.update { value }
    }

    private fun updateFilter(option: OrderFilter) {
        _filterOption.value = option
    }

    private fun getUserOrders(){
        Timber.d("getUserOrders")
        viewModelScope.launch{
            setLoadingState(true)
            orderRepository.getUserOrders()
                .onError { result ->
                    SnackbarController.sendEvent(
                        event = SnackbarEvent(
                            error = result
                        )
                    )
                    setLoadingState(false)
                }
            setLoadingState(false)
        }
    }

    private fun setLoadingState(isLoading: Boolean) {
        _uiState.update { it.copy(isLoading = isLoading) }
    }


}