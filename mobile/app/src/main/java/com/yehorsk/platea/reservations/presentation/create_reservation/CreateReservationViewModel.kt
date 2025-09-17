package com.yehorsk.platea.reservations.presentation.create_reservation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yehorsk.platea.core.data.repository.MainPreferencesRepository
import com.yehorsk.platea.core.domain.remote.AppError
import com.yehorsk.platea.core.domain.remote.onError
import com.yehorsk.platea.core.domain.remote.onSuccess
import com.yehorsk.platea.core.domain.repository.RestaurantRepository
import com.yehorsk.platea.core.utils.ConnectivityObserver
import com.yehorsk.platea.core.utils.SideEffect
import com.yehorsk.platea.core.utils.snackbar.SnackbarController
import com.yehorsk.platea.core.utils.snackbar.SnackbarEvent
import com.yehorsk.platea.orders.presentation.create_order.OrderForm
import com.yehorsk.platea.reservations.domain.repository.ReservationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CreateReservationViewModel @Inject constructor(
    private val networkConnectivityObserver: ConnectivityObserver,
    private val reservationRepository: ReservationRepository,
    private val preferencesRepository: MainPreferencesRepository,
    private val restaurantRepository: RestaurantRepository
): ViewModel(){

    private val _sideEffectChannel = Channel<SideEffect>(capacity = Channel.BUFFERED)
    val sideEffectFlow: Flow<SideEffect>
        get() = _sideEffectChannel.receiveAsFlow()

    private val _uiState = MutableStateFlow(CreateReservationUiState())
    val uiState = _uiState.asStateFlow()

    init {
        observeRestaurantInfo()
        observeNetwork()
        observeUserCountryCode()
        observeUserPhone()
        getRestaurantInfo()
    }

    fun onAction(action: CreateReservationAction){
        when(action){
            CreateReservationAction.ClearForm -> clearForm()
            CreateReservationAction.CreateReservation -> createReservation()
            is CreateReservationAction.UpdatePartySize -> updatePartySize(action.size)
            is CreateReservationAction.UpdatePhone -> updatePhone(action.phone)
            is CreateReservationAction.UpdateReservationDate -> updateReservationDate(action.date)
            is CreateReservationAction.UpdateTimeSlot -> updateTimeSlot(action.id, action.time)
            is CreateReservationAction.ValidatePhoneNumber -> validatePhoneNumber(action.phone)
            is CreateReservationAction.UpdateWithOrder -> updateWithOrder(action.withOrder, action.form)
            is CreateReservationAction.UpdateSpecialRequest -> updateSpecialRequest(action.request)
        }
    }

    private fun getRestaurantInfo(){
        viewModelScope.launch {
            restaurantRepository.getRestaurantInfo()
        }
    }

    private fun observeRestaurantInfo(){
        restaurantRepository.getRestaurantInfoFlow()
            .onEach { info ->
                _uiState.update { it.copy(
                    restaurantInfo = info
                ) }
                Timber.d("Restaurant info: $info ")
            }
            .launchIn(viewModelScope)
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

    private fun observeUserCountryCode(){
        preferencesRepository.userCountryCodeFlow
            .onEach { code ->
                _uiState.update { state ->
                    val updatedCountryCode = code ?: ""
                    val updatedFullPhone = "${updatedCountryCode}${state.phone}"
                    state.copy(reservationForm = state.reservationForm.copy(fullPhone = updatedFullPhone))
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
                        reservationForm = state.reservationForm.copy(fullPhone = updatedFullPhone)
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun updatePartySize(size: Int){
        _uiState.update {
            it.copy(
                reservationForm = it.reservationForm.copy(
                    partySize = size
                )
            )
        }
        getAvailableTimeSlots()
    }

    fun updateSpecialRequest(request: String){
        _uiState.update {
            it.copy(
                reservationForm = it.reservationForm.copy(
                    specialRequest = request
                )
            )
        }
    }

    fun updateReservationDate(date: String){
        _uiState.update {
            it.copy(
                reservationForm = it.reservationForm.copy(
                    reservationDate = date
                )
            )
        }
        getAvailableTimeSlots()
    }

    fun updatePhone(phone: String){
        _uiState.update {
            it.copy(
                reservationForm = it.reservationForm.copy(
                    fullPhone = phone
                )
            )
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


    fun updateTimeSlot(slot: Int, time: String){
        _uiState.update {
            it.copy(
                reservationForm = it.reservationForm.copy(
                    selectedTimeSlot = slot,
                    selectedTime = time
                )
            )
        }
    }

    fun updateWithOrder(withOrder: Boolean, form: OrderForm){
        _uiState.update {
            it.copy(
                reservationForm = it.reservationForm.copy(
                    orderForm = form,
                    withOrder = withOrder
                )
            )
        }
    }

    fun getAvailableTimeSlots(){
        Timber.d("getAvailableTimeSlots")
        viewModelScope.launch{
            reservationRepository.getAvailableTimeSlots(uiState.value.reservationForm)
                .onSuccess { data, message ->
                    _uiState.update {
                        it.copy(timeSlots = data)
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

    fun getMaxCapacity(){
        Timber.d("getMaxCapacity")
        viewModelScope.launch{
            reservationRepository.getMaxCapacity()
                .onSuccess { data, _ ->
                    _uiState.update {
                        it.copy(maxCapacity = data.first())
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


    fun createReservation(){
        Timber.d("createReservation")
        viewModelScope.launch{
            reservationRepository.createReservation(uiState.value.reservationForm)
                .onSuccess { data, message ->
                    Timber.d("createReservation success -> $message")
                    SnackbarController.sendEvent(
                        event = SnackbarEvent(
                            message = message.toString()
                        )
                    )
                    _sideEffectChannel.send(SideEffect.NavigateToNextScreen)
                    clearForm()
                }
                .onError { error ->
                    Timber.d("createReservation error -> $error")
                    when(error){
                        is AppError.CONFLICT -> {
                            SnackbarController.sendEvent(
                                event = SnackbarEvent(
                                    message = error.message
                                )
                            )
                        }
                        else -> {
                            SnackbarController.sendEvent(
                                event = SnackbarEvent(
                                    error = error
                                )
                            )
                        }
                    }
                }
        }
    }

    fun clearForm(){
        _uiState.update {
            it.copy(
                timeSlots = null,
                reservationForm = ReservationForm()
            )
        }
        Timber.d("Reservation state: ${_uiState.value}")
    }

}