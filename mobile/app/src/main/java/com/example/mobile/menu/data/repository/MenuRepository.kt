package com.example.mobile.menu.data.repository

import com.example.mobile.menu.data.model.Menu
import kotlinx.coroutines.flow.Flow

interface MenuRepository {

    suspend fun getAllMenus(): List<Menu>

}