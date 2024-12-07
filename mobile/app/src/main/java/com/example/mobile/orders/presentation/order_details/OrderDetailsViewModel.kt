package com.example.mobile.orders.presentation.order_details

import androidx.lifecycle.viewModelScope
import com.example.mobile.core.data.remote.dto.NetworkResult
import com.example.mobile.core.domain.SideEffect
import com.example.mobile.core.utils.ConnectivityObserver
import com.example.mobile.orders.data.remote.OrderRepositoryImpl
import com.example.mobile.orders.data.remote.dto.toOrderEntity
import com.example.mobile.orders.presentation.OrderBaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class OrderDetailsViewModel @Inject constructor(
    networkConnectivityObserver: ConnectivityObserver,
    orderRepositoryImpl: OrderRepositoryImpl
): OrderBaseViewModel(networkConnectivityObserver, orderRepositoryImpl){

    fun getOrderDetails(){
        Timber.d("getOrderDetails ${_uiState.value.selectedItem}")
        viewModelScope.launch{
            isNetwork.collect{ available ->
                _uiState.update {
                    it.copy(isLoading = true)
                }
                if(available){
                    val result = orderRepositoryImpl.getUserOrderDetails(_uiState.value.selectedItem!!.id)
                    when(result){
                        is NetworkResult.Error ->{
                            _sideEffectChannel.send(SideEffect.ShowToast(result.message.toString()))
                        }
                        is NetworkResult.Success ->{
                            Timber.tag("Result Details: ${result.data}")
                            _uiState.update {
                                it.copy(selectedItem = result.data.first().toOrderEntity())
                            }
                        }
                    }
                } else {
                    _sideEffectChannel.send(SideEffect.ShowToast("No internet connection!"))
                }
                _uiState.update {
                    it.copy(isLoading = false)
                }
            }
        }
    }

}