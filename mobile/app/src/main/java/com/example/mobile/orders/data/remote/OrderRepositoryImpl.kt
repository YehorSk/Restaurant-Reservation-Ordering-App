package com.example.mobile.orders.data.remote

import com.example.mobile.core.data.remote.dto.NetworkResult
import com.example.mobile.orders.data.remote.dto.OrderMenuItemDto
import com.example.mobile.orders.domain.repository.OrderRepository
import com.example.mobile.orders.domain.service.OrderService
import com.example.mobile.utils.ConnectivityObserver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

class OrderRepositoryImpl @Inject constructor(
    private val orderService: OrderService,
    private val networkConnectivityObserver: ConnectivityObserver,
    ) : OrderRepository{

    private val isOnlineFlow: StateFlow<Boolean> = networkConnectivityObserver.observe()
        .distinctUntilChanged()
        .stateIn(
            scope = CoroutineScope(Dispatchers.IO),
            started = SharingStarted.Eagerly,
            initialValue = false
        )

    private suspend fun isOnline(): Boolean {
        return isOnlineFlow.first()
    }

    override suspend fun getUserOrderItems(): NetworkResult<List<OrderMenuItemDto>> {
        Timber.d("Repo getUserOrderItems")
        return if(isOnline()){
            try {
                val result = orderService.getUserOrderItems()
                NetworkResult.Success(data = result.data!!, message = "")
            }catch (e: HttpException){
                if(e.code() == 401){
                    NetworkResult.Error(code = 401, message = "No User")
                }else{
                    NetworkResult.Error(code = 520, message = e.message())
                }
            }
        }else{
            NetworkResult.Error(code = 503, message = "No internet connection!")
        }
    }

}