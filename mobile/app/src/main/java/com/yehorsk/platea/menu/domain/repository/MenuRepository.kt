package com.yehorsk.platea.menu.domain.repository

import com.yehorsk.platea.core.domain.remote.AppError
import com.yehorsk.platea.core.domain.remote.Result
import com.yehorsk.platea.menu.data.db.model.MenuItemEntity
import com.yehorsk.platea.menu.data.db.model.MenuWithMenuItems
import com.yehorsk.platea.menu.data.remote.dto.MenuDto
import kotlinx.coroutines.flow.Flow

interface MenuRepository {

    suspend fun getAllMenus(): Result<List<MenuDto>, AppError>

    suspend fun addFavorite(menuItemId: String): Result<List<String>, AppError>

    suspend fun deleteFavorite(menuItemId: String): Result<List<String>, AppError>

    fun getMenuWithMenuItems(): Flow<List<MenuWithMenuItems>>

    fun searchItems(text: String): Flow<List<MenuItemEntity>>

    fun getFavoriteItems(): Flow<List<MenuItemEntity>>

}