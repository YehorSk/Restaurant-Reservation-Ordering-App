package com.yehorsk.platea.menu.data.remote.service

import com.yehorsk.platea.core.data.remote.dto.ResponseDto
import com.yehorsk.platea.menu.data.remote.dto.MenuDto
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface MenuService {

    @GET("menu")
    suspend fun getAllMenus(): ResponseDto<MenuDto>

    @FormUrlEncoded
    @POST("favorite/user/add")
    suspend fun addFavoriteItem(@Field("menu_item_id") menuItemId: String): ResponseDto<String>

    @FormUrlEncoded
    @POST("favorite/user/delete")
    suspend fun deleteFavoriteItem(@Field("menu_item_id") menuItemId: String): ResponseDto<String>

}