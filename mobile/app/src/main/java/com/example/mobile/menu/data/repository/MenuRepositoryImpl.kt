package com.example.mobile.menu.data.repository

import com.example.mobile.auth.data.repository.AuthPreferencesRepository
import com.example.mobile.menu.data.model.Menu
import com.example.mobile.menu.data.model.MenuItemUser
import com.example.mobile.menu.data.service.MenuService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber
import javax.inject.Inject

class MenuRepositoryImpl @Inject constructor(
    private val menuService: MenuService,
    private val prefs: AuthPreferencesRepository,
) : MenuRepository {

    override suspend fun getAllMenus(): List<Menu> = menuService.getAllMenus()

    override suspend fun getAllItems(): List<MenuItemUser> {
        val token = prefs.jwtTokenFlow.first()
        Timber.d("Token $token")
        return menuService.getUserCartItems(token!!)
    }


}