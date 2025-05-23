package com.yehorsk.platea.orders.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.yehorsk.platea.orders.data.db.model.OrderEntity
import com.yehorsk.platea.orders.data.db.model.OrderItemEntity
import com.yehorsk.platea.orders.data.db.model.OrderWithOrderItems
import com.yehorsk.platea.reservations.data.db.model.ReservationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {

    @Query("SELECT * FROM order_table WHERE (:search = '' OR code LIKE '%' || :search || '%') AND (:filter = '' OR status LIKE :filter) ORDER BY date ASC, start_time ASC, end_time ASC;")
    fun getUserOrders(search: String = "", filter: String = ""): Flow<List<OrderEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: OrderEntity)

    @Update
    suspend fun updateOrder(order: OrderEntity)

    @Delete
    suspend fun deleteOrder(order: OrderEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrders(orders: List<OrderEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrderItem(item: OrderItemEntity)

    @Update
    suspend fun updateOrderItem(item: OrderItemEntity)

    @Delete
    suspend fun deleteOrderItem(item: OrderItemEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrderItems(items: List<OrderItemEntity>)

    @Transaction
    suspend fun runInTransaction(block: suspend () -> Unit) {
        block()
    }

    @Query("DELETE FROM cart_table")
    suspend fun deleteAllCartItems()

    @Transaction
    @Query("SELECT * FROM order_table")
    fun getOrderWithOrderItems(): Flow<List<OrderWithOrderItems>>


    @Transaction
    @Query("SELECT * FROM order_table WHERE id = :id")
    fun getOrderWithOrderItemsById(id: String): Flow<OrderWithOrderItems>

    @Transaction
    @Query("SELECT * FROM order_table ORDER BY created_at DESC")
    fun getOrderWithOrderItemsOnce(): List<OrderWithOrderItems>

    @Query("SELECT * FROM reservation_table ORDER BY created_at DESC")
    fun getUserReservations(): Flow<List<ReservationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReservation(reservation: ReservationEntity)

    @Update
    suspend fun updateReservation(reservation: ReservationEntity)

    @Delete
    suspend fun deleteReservation(reservation: ReservationEntity)


    
}