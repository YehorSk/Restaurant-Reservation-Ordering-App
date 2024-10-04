package com.example.mobile.menu.data.service

import com.example.mobile.common.NetworkResult
import com.example.mobile.menu.data.model.Menu
import com.example.mobile.menu.data.model.MenuItemUser
import com.example.mobile.ui.screens.client.home.viewmodel.CartForm
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface MenuService {

    @GET("menu")
    suspend fun getAllMenus(): List<Menu>

    @GET("user-cart-items")
    suspend fun getUserCartItems(@Header("Authorization") token: String) : List<MenuItemUser>

    @Headers(
        "Accept: application/vnd.api+json",
        "Content-Type: application/vnd.api+json"
    )
    @POST("add-user-cart-item")
    suspend fun addUserCartItem(@Header("Authorization") token: String,@Body cartForm: CartForm): String

}