package com.example.mobile.menu.data.remote

import com.example.mobile.auth.data.remote.AuthPreferencesRepository
import com.example.mobile.core.data.remote.dto.NetworkResult
import com.example.mobile.menu.data.remote.dto.Menu
import com.example.mobile.menu.data.service.MenuService
import com.example.mobile.menu.data.dao.MenuDao
import com.example.mobile.menu.data.repository.MenuRepository
import com.example.mobile.utils.ConnectivityRepository
import retrofit2.HttpException
import javax.inject.Inject

class MenuRepositoryImpl @Inject constructor(
    private val menuService: MenuService,
    private val prefs: AuthPreferencesRepository,
    private val menuDao: MenuDao,
    private val connectivityRepository: ConnectivityRepository
) : MenuRepository {

    override suspend fun getAllMenus(): NetworkResult<List<Menu>> {
        val isOnline = connectivityRepository.isInternetConnected()
        return if(isOnline){
            try {
                val result = menuService.getAllMenus()
                menuDao.syncMenusWithServer(result)
                NetworkResult.Success(data = result, message = "")
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