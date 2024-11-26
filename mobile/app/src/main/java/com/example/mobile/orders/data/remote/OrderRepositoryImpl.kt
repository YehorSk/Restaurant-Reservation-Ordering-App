package com.example.mobile.orders.data.remote

import com.example.mobile.cart.data.remote.dto.toCartItemEntity
import com.example.mobile.core.data.remote.dto.NetworkResult
import com.example.mobile.orders.data.remote.dto.OrderMenuItemDto
import com.example.mobile.orders.domain.repository.OrderRepository
import com.example.mobile.orders.domain.service.OrderService
import com.example.mobile.core.utils.ConnectivityObserver
import com.example.mobile.orders.data.remote.dto.OrderDto
import com.example.mobile.orders.presentation.create_order.OrderForm
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
import kotlin.collections.first

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

    override suspend fun getUserOrders(): NetworkResult<List<OrderDto>> {
        return if(isOnline()){
            try {
                val result = orderService.getUserOrders()
                NetworkResult.Success(data = result.data!!, message = result.message)
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

    override suspend fun makeUserPickUpOrder(orderForm: OrderForm): NetworkResult<OrderDto> {
        return if(isOnline()){
            try {
                val result = orderService.makeUserPickUpOrder(orderForm)
                NetworkResult.Success(data = result.data!!.first(), message = result.message)
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