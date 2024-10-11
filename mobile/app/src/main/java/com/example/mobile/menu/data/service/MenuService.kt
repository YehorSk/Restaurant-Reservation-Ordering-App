package com.example.mobile.menu.data.service

import com.example.mobile.menu.data.remote.model.Menu
import com.example.mobile.core.presentation.components.CartForm
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface MenuService {

    @GET("menu")
    suspend fun getAllMenus(): List<Menu>

}