package com.example.mobile.menu.data.service

import com.example.mobile.auth.data.model.HttpResponse
import com.example.mobile.menu.data.model.Menu
import com.example.mobile.menu.data.model.MenuItemUser
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers

interface MenuService {

    @GET("menu")
    suspend fun getAllMenus(): List<Menu>

    @Headers(
        "Accept: application/vnd.api+json",
        "Content-Type: application/vnd.api+json"
    )
    @GET("user-cart-items")
    suspend fun getUserCartItems(@Header("Authorization") token: String) : List<MenuItemUser>

}