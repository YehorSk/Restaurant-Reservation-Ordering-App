package com.example.mobile.orders.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.mobile.menu.data.db.model.MenuWithMenuItems
import com.example.mobile.orders.data.db.model.OrderEntity
import com.example.mobile.orders.data.db.model.OrderItemEntity
import com.example.mobile.orders.data.db.model.OrderWithOrderItems
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {

    @Query("SELECT * FROM order_table")
    fun getUserOrders(): Flow<List<OrderEntity>>

    @Insert
    suspend fun insertOrder(order: OrderEntity)

    @Update
    suspend fun updateOrder(order: OrderEntity)

    @Delete
    suspend fun deleteOrder(order: OrderEntity)

    @Insert
    suspend fun insertOrders(orders: List<OrderEntity>)

    @Insert
    suspend fun insertOrderItem(item: OrderItemEntity)

    @Update
    suspend fun updateOrderItem(item: OrderItemEntity)

    @Delete
    suspend fun deleteOrderItem(item: OrderItemEntity)

    @Insert
    suspend fun insertOrderItems(items: List<OrderItemEntity>)

    @Transaction
    suspend fun runInTransaction(block: suspend () -> Unit) {
        block()
    }

    @Transaction
    @Query("SELECT * FROM order_table")
    fun getOrderWithOrderItems(): Flow<List<OrderWithOrderItems>>

    @Transaction
    @Query("SELECT * FROM order_table")
    fun getOrderWithOrderItemsOnce(): List<OrderWithOrderItems>
}