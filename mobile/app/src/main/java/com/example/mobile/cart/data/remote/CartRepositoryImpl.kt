package com.example.mobile.cart.data.remote

import com.example.mobile.core.data.repository.MainPreferencesRepository
import com.example.mobile.cart.data.dao.CartDao
import com.example.mobile.cart.domain.repository.CartRepository
import com.example.mobile.cart.domain.service.CartService
import com.example.mobile.core.data.remote.dto.NetworkResult
import com.example.mobile.core.presentation.components.CartForm
import com.example.mobile.cart.data.remote.dto.CartItemDto
import com.example.mobile.cart.data.remote.dto.toCartItemEntity
import com.example.mobile.core.utils.ConnectivityObserver
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

class CartRepositoryImpl @Inject constructor(
    private val cartService: CartService,
    private val prefs: MainPreferencesRepository,
    private val networkConnectivityObserver: ConnectivityObserver,
    private val cartDao: CartDao
) : CartRepository {

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

    override suspend fun getAllItems(): NetworkResult<List<CartItemDto>> {
        Timber.d("Cart getAllItems")
        return if(isOnline()){
            try {
                Timber.d("Calling getUserCartItems")
                val result = cartService.getUserCartItems()
                Timber.d("items: $result.")
                NetworkResult.Success(data = result.data!!, message = "")
            }catch (e: HttpException){
                Timber.d("Error $e")
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

    override suspend fun addUserCartItem(cartForm: CartForm): NetworkResult<CartItemDto> {
        return if(isOnline()){
            try {
                val result = cartService.addUserCartItem(cartForm)
                Timber.d(result.toString())
                cartDao.insertItem(result.data!!.first().toCartItemEntity())
                NetworkResult.Success(data = result.data.first(), message = result.message)
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

    override suspend fun deleteUserCartItem(cartForm: CartForm): NetworkResult<CartItemDto> {
        return if(isOnline()){
            try {
                val result = cartService.deleteUserCartItem(cartForm)
                Timber.d(result.toString())
                cartDao.deleteItem(result.data!!.first().toCartItemEntity())
                NetworkResult.Success(data = result.data.first(), message = result.message)
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

    override suspend fun updateUserCartItem(cartForm: CartForm): NetworkResult<CartItemDto> {
        return if(isOnline()){
            try {
                val result = cartService.updateUserCartItem(cartForm)
                Timber.d(result.toString())
                cartDao.updateItem(result.data!!.first().toCartItemEntity())
                NetworkResult.Success(data = result.data.first(), message = result.message)
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