package com.yehorsk.platea.core.domain.repository

import com.yehorsk.platea.core.data.db.models.RestaurantInfoEntity
import com.yehorsk.platea.core.data.remote.dto.RestaurantInfoDto
import com.yehorsk.platea.core.domain.remote.AppError
import com.yehorsk.platea.core.domain.remote.Result
import kotlinx.coroutines.flow.Flow

interface RestaurantRepository {

    fun getRestaurantInfoFlow(): Flow<RestaurantInfoEntity?>

    suspend fun upsertRestaurantInfo(restaurantInfo: RestaurantInfoEntity)

    suspend fun getRestaurantInfo(): Result<List<RestaurantInfoDto>, AppError>

}