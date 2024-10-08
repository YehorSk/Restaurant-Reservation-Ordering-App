package com.example.mobile.menu.data.remote

import com.example.mobile.auth.data.remote.AuthPreferencesRepository
import com.example.mobile.core.data.remote.model.NetworkResult
import com.example.mobile.menu.data.remote.model.Menu
import com.example.mobile.menu.domain.service.MenuService
import com.example.mobile.core.presentation.components.CartForm
import com.example.mobile.menu.domain.repository.MenuRepository
import com.example.mobile.utils.ConnectivityRepository
import kotlinx.coroutines.flow.first
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

class MenuRepositoryImpl @Inject constructor(
    private val menuService: MenuService,
    private val prefs: AuthPreferencesRepository,
    private val connectivityRepository: ConnectivityRepository
) : MenuRepository {

    override suspend fun getAllMenus(): NetworkResult<List<Menu>> {
        val isOnline = connectivityRepository.isInternetConnected()
        return if(isOnline){
            try {
                val result = menuService.getAllMenus()
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

    override suspend fun addUserCartItem(cartForm: CartForm): NetworkResult<String> {
        val token = prefs.jwtTokenFlow.first()
        Timber.d("Token $token")
        val isOnline = connectivityRepository.isInternetConnected()
        return if(isOnline){
            try {
                if(token.isNullOrBlank()){
                    return NetworkResult.Error(code = 401, message = "No User")
                }
                val result = menuService.addUserCartItem("Bearer $token",cartForm)
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