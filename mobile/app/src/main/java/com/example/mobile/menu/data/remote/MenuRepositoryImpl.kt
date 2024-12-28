package com.example.mobile.menu.data.remote

import com.example.mobile.core.data.remote.safeCall
import com.example.mobile.core.domain.remote.AppError
import com.example.mobile.core.domain.remote.Result
import com.example.mobile.menu.data.remote.dto.MenuDto
import com.example.mobile.menu.domain.service.MenuService
import com.example.mobile.menu.data.dao.MenuDao
import com.example.mobile.menu.data.db.model.MenuItemEntity
import com.example.mobile.menu.data.remote.dto.toMenuEntity
import com.example.mobile.menu.data.remote.dto.toMenuItemEntity
import com.example.mobile.menu.domain.repository.MenuRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class MenuRepositoryImpl @Inject constructor(
    private val menuService: MenuService,
    private val menuDao: MenuDao,
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

    override suspend fun getAllMenus(): Result<List<MenuDto>, AppError> {
        Timber.d("Menu getAllMenus")
        return safeCall<MenuDto>(
            
            execute = {
                menuService.getAllMenus()
            },
            onSuccess = { data ->
                syncMenusWithServer(data)
            }
        )
    }

    override suspend fun addFavorite(menuItemId: String): Result<List<String>, AppError> {
        Timber.d("Menu addFavorite")
        return safeCall<String>(
            
            execute = {
                menuService.addFavoriteItem(menuItemId = menuItemId)
            },
            onSuccess = { data ->
                menuDao.addFavorite(menuItemId = menuItemId)
            }
        )
    }

    override suspend fun deleteFavorite(menuItemId: String): Result<List<String>, AppError> {
        Timber.d("Menu deleteFavorite")
        return safeCall<String>(
            
            execute = {
                menuService.deleteFavoriteItem(menuItemId = menuItemId)
            },
            onSuccess = { data ->
                menuDao.deleteFavorite(menuItemId = menuItemId)
            }
        )
    }
}