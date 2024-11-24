package com.example.mobile.menu.data.remote

import androidx.compose.runtime.collectAsState
import com.example.mobile.core.data.remote.dto.NetworkResult
import com.example.mobile.menu.data.remote.dto.MenuDto
import com.example.mobile.menu.domain.service.MenuService
import com.example.mobile.menu.data.dao.MenuDao
import com.example.mobile.menu.data.db.model.MenuItemEntity
import com.example.mobile.menu.data.remote.dto.toMenuEntity
import com.example.mobile.menu.data.remote.dto.toMenuItemEntity
import com.example.mobile.menu.domain.repository.MenuRepository
import com.example.mobile.utils.ConnectivityObserver
import com.example.mobile.utils.ConnectivityRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

class MenuRepositoryImpl @Inject constructor(
    private val menuService: MenuService,
    private val menuDao: MenuDao,
    private val networkConnectivityObserver: ConnectivityObserver,
) : MenuRepository {

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

    suspend fun syncMenusWithServer(serverMenuDtos: List<MenuDto>) = withContext(Dispatchers.IO) {
        val localMenus = menuDao.getMenuWithMenuItemsOnce()

        val serverMenuIds = serverMenuDtos.map { it.id }.toSet()
        val serverMenuItemsIds = serverMenuDtos.flatMap { menu -> menu.items.map { it.id } }.toSet()

        val menusToDelete = localMenus.filter { it.menu.id !in serverMenuIds }
        val menuItemsToDelete = localMenus.flatMap { localMenu ->
            localMenu.menuItems.filter { it.id !in serverMenuItemsIds }
        }

        menuDao.runInTransaction {
            menusToDelete.forEach { menu ->
                menuDao.deleteMenu(menu.menu)
                deleteMenuItems(menu.menuItems.map { it })
            }
            deleteMenuItems(menuItemsToDelete)
            insert(serverMenuDtos)
        }
    }

    suspend fun insert(menuDtos: List<MenuDto>) = withContext(Dispatchers.IO) {
        for(item in menuDtos){
            menuDao.insertMenu(item.toMenuEntity())
            for(menuItem in item.items){
                menuDao.insertMenuItem(menuItem.toMenuItemEntity())
            }
        }
    }

    suspend fun deleteMenuItems(menuItems: List<MenuItemEntity>) = withContext(Dispatchers.IO) {
        for (item in menuItems) {
            menuDao.deleteMenuItem(item)
        }
    }

    override suspend fun getAllMenus(): NetworkResult<List<MenuDto>> {
        Timber.d("getAllMenus Wifi status = ${isOnline()}")
        return if(isOnline()){
            try {
                val result = menuService.getAllMenus()
                syncMenusWithServer(result.data!!)
                NetworkResult.Success(data = result.data, message = "")
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

    override suspend fun addFavorite(menuItemId: String): NetworkResult<String> {
        Timber.d("addFavorite Wifi status = ${isOnline()}")
        return if(isOnline()){
            try {
                val result = menuService.addFavoriteItem(menuItemId = menuItemId)
                menuDao.addFavorite(menuItemId = menuItemId)
                NetworkResult.Success(data = "", message = result.message)
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

    override suspend fun deleteFavorite(menuItemId: String): NetworkResult<String> {
        Timber.d("deleteFavorite Wifi status = ${isOnline()}")
        return if(isOnline()){
            try {
                val result = menuService.deleteFavoriteItem(menuItemId = menuItemId)
                menuDao.deleteFavorite(menuItemId = menuItemId)
                NetworkResult.Success(data = "", message = result.message)
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