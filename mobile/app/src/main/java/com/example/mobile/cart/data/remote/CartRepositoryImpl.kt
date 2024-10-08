package com.example.mobile.cart.data.remote

import com.example.mobile.auth.data.remote.AuthPreferencesRepository
import com.example.mobile.cart.domain.repository.CartRepository
import com.example.mobile.cart.domain.service.CartService
import com.example.mobile.core.data.remote.model.NetworkResult
import com.example.mobile.core.presentation.components.CartForm
import com.example.mobile.cart.data.remote.model.CartItem
import com.example.mobile.utils.ConnectivityRepository
import kotlinx.coroutines.flow.first
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val cartService: CartService,
    private val prefs: AuthPreferencesRepository,
    private val connectivityRepository: ConnectivityRepository
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
                NetworkResult.Success(result)
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

    override suspend fun deleteUserCartItem(cartForm: CartForm): NetworkResult<String> {
        val token = prefs.jwtTokenFlow.first()
        Timber.d("Token $token")
        val isOnline = connectivityRepository.isInternetConnected()
        return if(isOnline){
            try {
                if(token.isNullOrBlank()){
                    return NetworkResult.Error(code = 401, message = "No User")
                }
                val result = cartService.deleteUserCartItem("Bearer $token",cartForm)
                Timber.d(result)
                NetworkResult.Success(result)
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

    override suspend fun updateUserCartItem(cartForm: CartForm): NetworkResult<String> {
        val token = prefs.jwtTokenFlow.first()
        Timber.d("Token $token")
        val isOnline = connectivityRepository.isInternetConnected()
        return if(isOnline){
            try {
                if(token.isNullOrBlank()){
                    return NetworkResult.Error(code = 401, message = "No User")
                }
                val result = cartService.updateUserCartItem("Bearer $token",cartForm)
                Timber.d(result)
                NetworkResult.Success(result)
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