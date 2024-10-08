package com.example.mobile.menu.domain.repository

import com.example.mobile.core.data.remote.model.NetworkResult
import com.example.mobile.menu.data.remote.model.Menu
import com.example.mobile.core.presentation.components.CartForm

interface MenuRepository {

    suspend fun getAllMenus(): NetworkResult<List<Menu>>

    suspend fun addUserCartItem(cartForm: CartForm): NetworkResult<String>

}