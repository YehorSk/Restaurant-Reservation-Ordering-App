package com.yehorsk.platea.orders.presentation.create_order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yehorsk.platea.cart.domain.models.CartItem
import com.yehorsk.platea.core.data.dao.RestaurantInfoDao
import com.yehorsk.platea.core.data.db.models.RestaurantInfoEntity
import com.yehorsk.platea.core.data.repository.MainPreferencesRepository
import com.yehorsk.platea.core.domain.remote.onError
import com.yehorsk.platea.core.domain.remote.onSuccess
import com.yehorsk.platea.core.utils.ConnectivityObserver
import com.yehorsk.platea.core.utils.SideEffect
import com.yehorsk.platea.core.utils.Utility.getEndTime
import com.yehorsk.platea.core.utils.Utility.getStartTime
import com.yehorsk.platea.core.utils.snackbar.SnackbarController
import com.yehorsk.platea.core.utils.snackbar.SnackbarEvent
import com.yehorsk.platea.orders.data.remote.dto.TableDto
import com.yehorsk.platea.orders.domain.repository.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CreateOrderViewModel @Inject constructor(
    private val networkConnectivityObserver: ConnectivityObserver,
    private val orderRepository: OrderRepository,
    private val preferencesRepository: MainPreferencesRepository,
    private val restaurantInfoDao: RestaurantInfoDao
): ViewModel(){

    val restaurantInfoUiState: StateFlow<RestaurantInfoEntity?> = restaurantInfoDao.getRestaurantInfo()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    val cartItemsUiState: StateFlow<List<CartItem>> = orderRepository.getAllCartItems()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = listOf()
        )

    private val _sideEffectChannel = Channel<SideEffect>(capacity = Channel.BUFFERED)
    val sideEffectFlow: Flow<SideEffect>
        get() = _sideEffectChannel.receiveAsFlow()

    private val _uiState = MutableStateFlow(CreateOrderUiState())
    val uiState = _uiState.asStateFlow()

    init {
        observeNetwork()
        observeUserRole()
        observeUserAddress()
        observeUserCountryCode()
        observeUserPhone()
    }


    fun onAction(action: CreateOrderAction){
        when(action){
            CreateOrderAction.MakeOrder -> makeOrder()
            is CreateOrderAction.UpdateAddress -> updateAddress(action.address)
            is CreateOrderAction.UpdateInstructions -> updateInstructions(action.instructions)
            is CreateOrderAction.UpdateOrderType -> updateOrderType(
                type = action.type,
                text = action.text
            )
            is CreateOrderAction.UpdateRequest -> updateRequest(action.request)
            is CreateOrderAction.MakeWaiterOrder -> makeWaiterOrder()
            CreateOrderAction.CloseBottomSheet -> closeBottomSheet()
            CreateOrderAction.OpenBottomSheet -> showBottomSheet()
            is CreateOrderAction.UpdateTime -> updateTime(start = action.start, end = action.end)
            is CreateOrderAction.UpdateDate -> updateDate(date = action.date)
            is CreateOrderAction.UpdatePhone -> updatePhone(phone = action.phone)
            is CreateOrderAction.ValidatePhone -> validatePhoneNumber(action.phone)
        }
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

    private fun observeUserAddress(){
        preferencesRepository.userAddressFlow
            .onEach { address ->
                _uiState.update { it.copy(orderForm = it.orderForm.copy(address = address ?: "")) }
            }
            .launchIn(viewModelScope)
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

    private fun observeUserCountryCode(){
        preferencesRepository.userCountryCodeFlow
            .onEach { code ->
                _uiState.update { state ->
                    val updatedCountryCode = code ?: ""
                    val updatedFullPhone = "${updatedCountryCode}${state.phone}"
                    state.copy(orderForm = state.orderForm.copy(fullPhone = updatedFullPhone))
                    state.copy(countryCode = updatedCountryCode)
                }
            }
            .launchIn(viewModelScope)
    }

    private fun observeUserPhone(){
        preferencesRepository.userPhoneFlow
            .onEach { phone ->
                _uiState.update { state ->
                    val updatedPhone = phone ?: ""
                    val updatedFullPhone = "${state.countryCode}${updatedPhone}"
                    state.copy(
                        phone = updatedPhone,
                        orderForm = state.orderForm.copy(fullPhone = updatedFullPhone)
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun showBottomSheet(){
        _uiState.update {
            it.copy(showBottomSheet = true)
        }
    }

    fun closeBottomSheet(){
        _uiState.update {
            it.copy(showBottomSheet = false)
        }
    }

    fun validatePhoneNumber(phone: String){
        val phoneRegex = "^[+]?[0-9]{10,15}$"
        _uiState.update { currentState ->
            currentState.copy(
                isPhoneValid = phone.matches(phoneRegex.toRegex())
            )
        }
    }

    fun updatePhone(phone: String){
        _uiState.update {
            it.copy(
                orderForm = it.orderForm.copy(
                    fullPhone = phone
                )
            )
        }
    }

    fun updateOrderType(type: Int, text: String){
        _uiState.update {
            it.copy(
                orderForm = it.orderForm.copy(
                                    orderType = type,
                                    orderText = text
                    )
                )
        }
    }

    fun updateTime(start: String, end: String){
        _uiState.update {
            it.copy(
                orderForm = it.orderForm.copy(
                    startTime = start,
                    endTime = end
                )
            )
        }
    }

    fun updateDate(date: String){
        Timber.d("updateDate: $date")
        _uiState.update {
            it.copy(
                orderForm = it.orderForm.copy(
                    selectedDate = date
                )
            )
        }
    }

    fun updateRequest(request: String){
        _uiState.update {
            it.copy(
                orderForm = it.orderForm.copy(
                    specialRequest = request
                )
            )
        }
    }

    fun updateAddress(address: String){
        _uiState.update {
            it.copy(
                orderForm = it.orderForm.copy(
                    address = address
                )
            )
        }
    }

    fun updateInstructions(instructions: String){
        _uiState.update {
            it.copy(
                orderForm = it.orderForm.copy(
                    instructions = instructions
                )
            )
        }
    }

    fun updateTableNumber(table: TableDto){
        _uiState.update {
            it.copy(
                orderForm = it.orderForm.copy(
                    selectedTable = table
                )
            )
        }
    }

    fun validateForm(): Boolean{
        with(_uiState.value.orderForm){
            return if(orderType == 0){
                true
            }else if(orderType == 1){
                instructions.isNotBlank() && address.isNotBlank() && _uiState.value.isPhoneValid
            }else if(orderType == 2){
                true
            }else if(orderType == 3){
                _uiState.value.orderForm.selectedTable != null
            }else{
                false
            }
        }
    }

    fun makeOrder(){
        with(_uiState.value.orderForm){
            if(orderType == 0){ // Pickup User
                makePickupOrder()
            }else if(orderType == 1){ // Delivery User
                makeDeliveryOrder()
            }else{
                null
            }
        }
    }

    fun getUserOrderItems(){
        Timber.d("getUserOrderItems")
        viewModelScope.launch{
            orderRepository.getUserOrderItems()
                .onSuccess { data, message ->
                    _uiState.update {
                        it.copy(orderItems = data)
                    }
                }
                .onError { error ->
                    SnackbarController.sendEvent(
                        event = SnackbarEvent(
                            error = error
                        )
                    )
                }
        }
    }

    fun makePickupOrder(){
        Timber.d("makePickupOrder")
        viewModelScope.launch{
            orderRepository.makeUserPickUpOrder(uiState.value.orderForm)
                .onSuccess { data, message ->
                    SnackbarController.sendEvent(
                        event = SnackbarEvent(
                            message = message.toString()
                        )
                    )
                    _sideEffectChannel.send(SideEffect.NavigateToNextScreen)
                }
                .onError { error ->
                    SnackbarController.sendEvent(
                        event = SnackbarEvent(
                            error = error
                        )
                    )
                }
        }
    }

    fun makeDeliveryOrder(){
        Timber.d("makeDeliveryOrder")
        viewModelScope.launch{
            orderRepository.makeUserDeliveryOrder(uiState.value.orderForm)
                .onSuccess { data, message ->
                    SnackbarController.sendEvent(
                        event = SnackbarEvent(
                            message = message.toString()
                        )
                    )
                    _sideEffectChannel.send(SideEffect.NavigateToNextScreen)
                }
                .onError { error ->
                    SnackbarController.sendEvent(
                        event = SnackbarEvent(
                            error = error
                        )
                    )
                }
        }
    }

    fun makeWaiterOrder(){
        Timber.d("makeWaiterOrder")
        viewModelScope.launch{
            _uiState.update { state ->
                state.copy(
                    orderForm = _uiState.value.orderForm.copy(
                        startTime = getStartTime(),
                        endTime = getEndTime()
                    )
                )
            }
            orderRepository.makeWaiterOrder(uiState.value.orderForm)
                .onSuccess { data, message ->
                    SnackbarController.sendEvent(
                        event = SnackbarEvent(
                            message = message.toString()
                        )
                    )
                    _sideEffectChannel.send(SideEffect.NavigateToNextScreen)
                }
                .onError { error ->
                    SnackbarController.sendEvent(
                        event = SnackbarEvent(
                            error = error
                        )
                    )
                }
        }
    }

    fun getTables(){
        Timber.d("getTables")
        viewModelScope.launch{
            setLoadingState(true)
            orderRepository.getTables()
                .onSuccess { data, message ->
                    _uiState.update {
                        it.copy(tables = data)
                    }
                    setLoadingState(false)
                }
                .onError { error ->
                    SnackbarController.sendEvent(
                        event = SnackbarEvent(
                            error = error
                        )
                    )
                    setLoadingState(false)
                }
        }
    }

    protected fun setLoadingState(isLoading: Boolean) {
        _uiState.update { it.copy(isLoading = isLoading) }
    }

}