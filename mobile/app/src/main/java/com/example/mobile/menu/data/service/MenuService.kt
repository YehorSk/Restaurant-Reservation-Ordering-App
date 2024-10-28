package com.example.mobile.menu.data.service

import com.example.mobile.menu.data.remote.dto.Menu
import retrofit2.http.GET

interface MenuService {

    @GET("menu")
    suspend fun getAllMenus(): List<Menu>

}