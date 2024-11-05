package com.example.mobile.menu.data.remote

import com.example.mobile.core.data.repository.MainPreferencesRepository
import com.example.mobile.core.data.remote.dto.NetworkResult
import com.example.mobile.menu.data.remote.dto.MenuDto
import com.example.mobile.menu.data.service.MenuService
import com.example.mobile.menu.data.dao.MenuDao
import com.example.mobile.menu.data.db.model.MenuItemEntity
import com.example.mobile.menu.data.remote.dto.toMenuEntity
import com.example.mobile.menu.data.remote.dto.toMenuItemEntity
import com.example.mobile.menu.data.repository.MenuRepository
import com.example.mobile.utils.ConnectivityRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import javax.inject.Inject

class MenuRepositoryImpl @Inject constructor(
    private val menuService: MenuService,
    private val menuDao: MenuDao,
    private val connectivityRepository: ConnectivityRepository
) : MenuRepository {

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
        val isOnline = connectivityRepository.isInternetConnected()
        return if(isOnline){
            try {
                val result = menuService.getAllMenus()
                syncMenusWithServer(result)
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

    override suspend fun addFavorite(menuItemId: String): NetworkResult<String> {
        val isOnline = connectivityRepository.isInternetConnected()
        return if(isOnline){
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
        val isOnline = connectivityRepository.isInternetConnected()
        return if(isOnline){
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