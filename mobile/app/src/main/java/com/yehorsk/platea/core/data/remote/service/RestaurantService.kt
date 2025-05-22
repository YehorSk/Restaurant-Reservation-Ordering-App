package com.yehorsk.platea.core.data.remote.service

import com.yehorsk.platea.core.data.remote.dto.ResponseDto
import com.yehorsk.platea.core.data.remote.dto.RestaurantInfoDto
import retrofit2.http.GET

interface RestaurantService {

    @GET("get-restaurant-info")
    suspend fun getRestaurantInfo(): ResponseDto<RestaurantInfoDto>

}