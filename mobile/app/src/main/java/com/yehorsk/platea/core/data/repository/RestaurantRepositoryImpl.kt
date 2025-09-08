package com.yehorsk.platea.core.data.repository

import com.yehorsk.platea.core.data.dao.RestaurantInfoDao
import com.yehorsk.platea.core.data.db.models.RestaurantInfoEntity
import com.yehorsk.platea.core.data.remote.dto.RestaurantInfoDto
import com.yehorsk.platea.core.data.remote.dto.toRestaurantInfoEntity
import com.yehorsk.platea.core.data.remote.service.RestaurantService
import com.yehorsk.platea.core.data.remote.service.safeCall
import com.yehorsk.platea.core.domain.remote.AppError
import com.yehorsk.platea.core.domain.remote.Result
import com.yehorsk.platea.core.domain.remote.onSuccess
import com.yehorsk.platea.core.domain.repository.RestaurantRepository
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import javax.inject.Inject

class RestaurantRepositoryImpl @Inject constructor(
    private val restaurantInfoDao: RestaurantInfoDao,
    private val restaurantService: RestaurantService
): RestaurantRepository {

    override fun getRestaurantInfoFlow(): Flow<RestaurantInfoEntity?> {
        return restaurantInfoDao.getRestaurantInfo()
    }

    override suspend fun upsertRestaurantInfo(restaurantInfo: RestaurantInfoEntity) {
        return restaurantInfoDao.upsertRestaurantInfo(restaurantInfo)
    }

    override suspend fun getRestaurantInfo(): Result<List<RestaurantInfoDto>, AppError> {
        Timber.d("getRestaurantInfo")
        return safeCall<RestaurantInfoDto>(
            execute = {
                restaurantService.getRestaurantInfo()
            }
        ).onSuccess { data, _ ->
            Timber.d("Restaurant $data")
            restaurantInfoDao.upsertRestaurantInfo(data.first().toRestaurantInfoEntity())
        }
    }
}