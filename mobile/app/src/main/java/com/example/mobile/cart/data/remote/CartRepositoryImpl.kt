package com.example.mobile.cart.data.remote

import com.example.mobile.core.data.repository.MainPreferencesRepository
import com.example.mobile.cart.data.dao.CartDao
import com.example.mobile.cart.domain.repository.CartRepository
import com.example.mobile.cart.domain.service.CartService
import com.example.mobile.core.data.remote.dto.NetworkResult
import com.example.mobile.core.presentation.components.CartForm
import com.example.mobile.cart.data.remote.dto.CartItemDto
import com.example.mobile.cart.data.remote.dto.toCartItemEntity
import com.example.mobile.utils.ConnectivityRepository
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val cartService: CartService,
    private val prefs: MainPreferencesRepository,
    private val connectivityRepository: ConnectivityRepository,
    private val cartDao: CartDao
)
    : CartRepository {

    override suspend fun getAllItems(): NetworkResult<List<CartItemDto>> {
        Timber.d("Cart getAllItems")
        val isOnline = connectivityRepository.isInternetConnected()
        return if(isOnline){
            try {
                Timber.d("Calling getUserCartItems")
                val result = cartService.getUserCartItems()
                Timber.d("items: $result")
                NetworkResult.Success(data = result, message = "")
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
        val isOnline = connectivityRepository.isInternetConnected()
        return if(isOnline){
            try {
                val result = cartService.addUserCartItem(cartForm)
                Timber.d(result.toString())
                cartDao.insertItem(result.data!!.toCartItemEntity())
                NetworkResult.Success(data = result.data, message = result.message)
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
        val isOnline = connectivityRepository.isInternetConnected()
        return if(isOnline){
            try {
                val result = cartService.deleteUserCartItem(cartForm)
                Timber.d(result.toString())
                cartDao.deleteItem(result.data!!.toCartItemEntity())
                NetworkResult.Success(data = result.data, message = result.message)
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
        val isOnline = connectivityRepository.isInternetConnected()
        return if(isOnline){
            try {
                val result = cartService.updateUserCartItem(cartForm)
                Timber.d(result.toString())
                cartDao.updateItem(result.data!!.toCartItemEntity())
                NetworkResult.Success(data = result.data, message = result.message)
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