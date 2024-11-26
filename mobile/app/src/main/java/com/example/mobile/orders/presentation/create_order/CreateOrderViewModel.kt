package com.example.mobile.orders.presentation.create_order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile.core.data.remote.dto.NetworkResult
import com.example.mobile.core.domain.SideEffect
import com.example.mobile.orders.data.remote.OrderRepositoryImpl
import com.example.mobile.core.utils.ConnectivityObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CreateOrderViewModel @Inject constructor(
    val networkConnectivityObserver: ConnectivityObserver,
    val orderRepositoryImpl: OrderRepositoryImpl
): ViewModel(){

    private val _uiState = MutableStateFlow(CreateOrderUiState())
    val uiState = _uiState.asStateFlow()

    val isNetwork = networkConnectivityObserver.observe()

    protected val _sideEffectChannel = Channel<SideEffect>(capacity = Channel.BUFFERED)
    val sideEffectFlow: Flow<SideEffect>
        get() = _sideEffectChannel.receiveAsFlow()

    init{
        viewModelScope.launch{
            isNetwork.collect{ available ->
                if(!available){
                    _sideEffectChannel.send(SideEffect.ShowToast("No internet connection!"))
                }
            }
        }
    }

    fun updateOrderType(type: Int, text: String){
        _uiState.update {
            it.copy(
                orderType = type,
                orderText = text
                )
        }
    }

    fun updateRequest(request: String){
        _uiState.update {
            it.copy(
                orderForm = OrderForm(
                    specialRequest = request
                )
            )
        }
    }

    fun getUserOrderItems(){
        Timber.d("getUserOrderItems")
        viewModelScope.launch{
            isNetwork.collect{ available ->
                if(available){
                    val result = orderRepositoryImpl.getUserOrderItems()
                    when(result){
                        is NetworkResult.Error ->{
                            if(result.code == 503){
                                _sideEffectChannel.send(SideEffect.ShowToast("No internet connection!"))
                            }else{
                                _sideEffectChannel.send(SideEffect.ShowToast(result.message.toString()))
                            }
                        }
                        is NetworkResult.Success ->{
                            _uiState.update {
                                it.copy(items = result.data)
                            }
                        }
                    }
                } else {
                    _sideEffectChannel.send(SideEffect.ShowToast("No internet connection!"))
                }
            }
        }
    }


}