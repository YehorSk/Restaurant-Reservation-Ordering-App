package com.example.mobile.menu.domain.repository

import com.example.mobile.core.data.remote.dto.NetworkResult
import com.example.mobile.menu.data.remote.dto.MenuDto

interface MenuRepository {

    suspend fun getAllMenus(): NetworkResult<List<MenuDto>>

    suspend fun addFavorite(menuItemId: String): NetworkResult<String>

    suspend fun deleteFavorite(menuItemId: String): NetworkResult<String>

}