package com.yehorsk.platea.menu.domain.repository

import com.yehorsk.platea.core.domain.remote.AppError
import com.yehorsk.platea.core.domain.remote.Result
import com.yehorsk.platea.menu.data.remote.dto.MenuDto
import com.yehorsk.platea.menu.domain.models.Menu
import com.yehorsk.platea.menu.domain.models.MenuItem
import kotlinx.coroutines.flow.Flow

interface MenuRepository {

    suspend fun getAllMenus(): Result<List<MenuDto>, AppError>

    suspend fun addFavorite(menuItemId: String): Result<List<String>, AppError>

    suspend fun deleteFavorite(menuItemId: String): Result<List<String>, AppError>

    fun getMenuWithMenuItems(): Flow<List<Menu>>

    fun searchItems(text: String): Flow<List<MenuItem>>

    fun getFavoriteItems(): Flow<List<MenuItem>>

}