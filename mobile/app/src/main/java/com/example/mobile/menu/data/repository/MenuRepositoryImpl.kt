package com.example.mobile.menu.data.repository

import com.example.mobile.menu.data.model.Menu
import com.example.mobile.menu.data.service.MenuService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class MenuRepositoryImpl @Inject constructor(
    private val menuService: MenuService
) : MenuRepository {

    override suspend fun getAllMenus(): List<Menu> = menuService.getAllMenus()

}