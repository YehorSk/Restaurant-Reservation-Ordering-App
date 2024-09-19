package com.example.mobile.menu.data.service

import com.example.mobile.menu.data.model.Menu
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Headers

interface MenuService {

    @GET("menu")
    suspend fun getAllMenus(): List<Menu>

}