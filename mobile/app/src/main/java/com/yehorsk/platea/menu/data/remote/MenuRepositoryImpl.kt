package com.yehorsk.platea.menu.data.remote

import com.yehorsk.platea.core.data.remote.service.safeCall
import com.yehorsk.platea.core.domain.remote.AppError
import com.yehorsk.platea.core.domain.remote.Result
import com.yehorsk.platea.menu.data.dao.MenuDao
import com.yehorsk.platea.menu.data.db.model.MenuItemEntity
import com.yehorsk.platea.menu.data.db.model.MenuWithMenuItems
import com.yehorsk.platea.menu.data.mappers.toMenu
import com.yehorsk.platea.menu.data.mappers.toMenuItem
import com.yehorsk.platea.menu.data.remote.dto.MenuDto
import com.yehorsk.platea.menu.data.remote.dto.toMenuEntity
import com.yehorsk.platea.menu.data.remote.dto.toMenuItemEntity
import com.yehorsk.platea.menu.data.remote.service.MenuService
import com.yehorsk.platea.menu.domain.models.Menu
import com.yehorsk.platea.menu.domain.models.MenuItem
import com.yehorsk.platea.menu.domain.repository.MenuRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class MenuRepositoryImpl @Inject constructor(
    private val menuService: MenuService,
    private val menuDao: MenuDao,
) : MenuRepository {

    private suspend fun syncMenusWithServer(serverMenuDtos: List<MenuDto>) =
        withContext(Dispatchers.IO) {
            val localMenus = menuDao.getMenuWithMenuItemsOnce()

            val serverMenuIds = serverMenuDtos.map { it.id }.toSet()
            val serverMenuItemsIds =
                serverMenuDtos.flatMap { menu -> menu.items.map { it.id } }.toSet()

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
        for (item in menuDtos) {
            menuDao.insertMenu(item.toMenuEntity())
            for (menuItem in item.items) {
                menuDao.insertMenuItem(menuItem.toMenuItemEntity())
            }
        }
    }

    private suspend fun deleteMenuItems(menuItems: List<MenuItemEntity>) =
        withContext(Dispatchers.IO) {
            for (item in menuItems) {
                menuDao.deleteMenuItem(item)
            }
        }

    override suspend fun getAllMenus(): Result<List<MenuDto>, AppError> {
        Timber.Forest.d("Menu getAllMenus")
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
        Timber.Forest.d("Menu addFavorite")
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
        Timber.Forest.d("Menu deleteFavorite")
        return safeCall<String>(
            execute = {
                menuService.deleteFavoriteItem(menuItemId = menuItemId)
            },
            onSuccess = { _ ->
                menuDao.deleteFavorite(menuItemId = menuItemId)
            }
        )
    }

    override fun getMenuWithMenuItems(): Flow<List<Menu>> {
        return menuDao
            .getMenuWithMenuItems()
            .map { entities ->
                entities.map {
                    it.toMenu()
                }
            }
    }

    override fun searchItems(text: String): Flow<List<MenuItem>> {
        return menuDao
            .searchItems(text)
            .map { entities ->
                entities.map {
                    it.toMenuItem()
                }
            }
    }

    override fun getFavoriteItems(): Flow<List<MenuItem>> {
        return menuDao
            .getFavoriteItems()
            .map { entities ->
                entities.map {
                    it.toMenuItem()
                }
            }
    }
}