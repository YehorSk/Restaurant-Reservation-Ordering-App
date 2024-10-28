package com.example.mobile.menu.data.repository

import com.example.mobile.core.data.remote.dto.NetworkResult
import com.example.mobile.menu.data.remote.dto.Menu

interface MenuRepository {

    suspend fun getAllMenus(): NetworkResult<List<Menu>>

}