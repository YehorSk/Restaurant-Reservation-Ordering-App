package com.example.mobile.cart.data.remote

import com.example.mobile.auth.data.remote.AuthPreferencesRepository
import com.example.mobile.cart.data.dao.CartDao
import com.example.mobile.cart.data.repository.CartRepository
import com.example.mobile.cart.data.service.CartService
import com.example.mobile.core.data.remote.model.NetworkResult
import com.example.mobile.core.presentation.components.CartForm
import com.example.mobile.cart.data.remote.model.CartItem
import com.example.mobile.cart.data.remote.model.toCartItemEntity
import com.example.mobile.core.data.remote.ApiHandler
import com.example.mobile.utils.ConnectivityRepository
import kotlinx.coroutines.flow.first
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val cartService: CartService,
    private val prefs: AuthPreferencesRepository,
    private val connectivityRepository: ConnectivityRepository,
    private val cartDao: CartDao
)
    : CartRepository {

    override suspend fun getAllItems(): NetworkResult<List<CartItem>> {
        val token = prefs.jwtTokenFlow.first()
        Timber.d("Token $token")
        val isOnline = connectivityRepository.isInternetConnected()
        return if(isOnline){
            try {
                if(token.isNullOrBlank()){
                    return NetworkResult.Error(code = 401, message = "No User")
                }
                val result = cartService.getUserCartItems("Bearer $token")
                NetworkResult.Success(status = "200", data = result, message = "")
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

    override suspend fun addUserCartItem(cartForm: CartForm): NetworkResult<CartItem> {
        val token = prefs.jwtTokenFlow.first()
        Timber.d("Token $token")
        val isOnline = connectivityRepository.isInternetConnected()
        return if(isOnline){
            try {
                if(token.isNullOrBlank()){
                    return NetworkResult.Error(code = 401, message = "No User")
                }
                val result = cartService.addUserCartItem("Bearer $token",cartForm)
                Timber.d(result.toString())
                cartDao.insertItem(result.data.toCartItemEntity())
                NetworkResult.Success(status = result.status, data = result.data, message = result.message)
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

    override suspend fun deleteUserCartItem(cartForm: CartForm): NetworkResult<CartItem> {
        val token = prefs.jwtTokenFlow.first()
        Timber.d("Token $token")
        val isOnline = connectivityRepository.isInternetConnected()
        return if(isOnline){
            try {
                if(token.isNullOrBlank()){
                    return NetworkResult.Error(code = 401, message = "No User")
                }
                val result = cartService.deleteUserCartItem("Bearer $token",cartForm)
                Timber.d(result.toString())
                cartDao.deleteItem(result.data.toCartItemEntity())
                NetworkResult.Success(status = result.status, data = result.data, message = result.message)
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

    override suspend fun updateUserCartItem(cartForm: CartForm): NetworkResult<CartItem> {
        val token = prefs.jwtTokenFlow.first()
        Timber.d("Token $token")
        val isOnline = connectivityRepository.isInternetConnected()
        return if(isOnline){
            try {
                if(token.isNullOrBlank()){
                    return NetworkResult.Error(code = 401, message = "No User")
                }
                val result = cartService.updateUserCartItem("Bearer $token",cartForm)
                Timber.d(result.toString())
                cartDao.updateItem(result.data.toCartItemEntity())
                NetworkResult.Success(status = result.status, data = result.data, message = result.message)
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