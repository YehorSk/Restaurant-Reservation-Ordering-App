package com.example.mobile.menu.domain.repository

import com.example.mobile.core.data.remote.dto.NetworkResult
import com.example.mobile.core.domain.AppError
import com.example.mobile.core.domain.Result
import com.example.mobile.menu.data.remote.dto.MenuDto

interface MenuRepository {

    suspend fun getAllMenus(): Result<List<MenuDto>, AppError>

    suspend fun addFavorite(menuItemId: String): Result<List<String>, AppError>

    suspend fun deleteFavorite(menuItemId: String): Result<List<String>, AppError>

}