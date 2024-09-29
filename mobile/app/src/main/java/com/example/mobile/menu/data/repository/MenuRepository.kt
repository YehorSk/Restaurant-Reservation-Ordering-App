package com.example.mobile.menu.data.repository

import com.example.mobile.common.NetworkResult
import com.example.mobile.menu.data.model.Menu
import com.example.mobile.menu.data.model.MenuItemUser
import com.example.mobile.ui.screens.client.home.viewmodel.CartForm
import kotlinx.coroutines.flow.Flow

interface MenuRepository {

    suspend fun getAllMenus(): NetworkResult<List<Menu>>

    suspend fun getAllItems(): NetworkResult<List<MenuItemUser>>

    suspend fun addUserCartItem(cartForm: CartForm): NetworkResult<String>

}