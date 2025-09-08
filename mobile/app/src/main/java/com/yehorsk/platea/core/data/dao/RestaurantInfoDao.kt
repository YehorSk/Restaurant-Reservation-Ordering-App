package com.yehorsk.platea.core.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.yehorsk.platea.core.data.db.models.RestaurantInfoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RestaurantInfoDao {

    @Query("SELECT * FROM restaurant_info LIMIT 1")
    fun getRestaurantInfo(): Flow<RestaurantInfoEntity?>

    @Upsert
    suspend fun upsertRestaurantInfo(restaurantInfo: RestaurantInfoEntity)

}