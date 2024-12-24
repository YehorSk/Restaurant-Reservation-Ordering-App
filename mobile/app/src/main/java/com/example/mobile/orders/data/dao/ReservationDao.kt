package com.example.mobile.orders.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.mobile.menu.data.db.model.MenuWithMenuItems
import com.example.mobile.orders.data.db.model.OrderEntity
import com.example.mobile.orders.data.db.model.OrderItemEntity
import com.example.mobile.orders.data.db.model.OrderWithOrderItems
import com.example.mobile.orders.data.db.model.ReservationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReservationDao {

    @Query("SELECT * FROM reservation_table ORDER BY created_at DESC")
    fun getUserReservations(): Flow<List<ReservationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReservation(reservation: ReservationEntity)

    @Update
    suspend fun updateReservation(reservation: ReservationEntity)

    @Delete
    suspend fun deleteReservation(reservation: ReservationEntity)
    
}