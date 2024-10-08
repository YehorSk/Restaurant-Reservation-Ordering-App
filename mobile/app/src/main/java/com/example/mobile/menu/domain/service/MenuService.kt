package com.example.mobile.menu.domain.service

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

    @Headers(
        "Accept: application/vnd.api+json",
        "Content-Type: application/vnd.api+json"
    )
    @POST("add-user-cart-item")
    suspend fun addUserCartItem(@Header("Authorization") token: String,@Body cartForm: CartForm): String

}