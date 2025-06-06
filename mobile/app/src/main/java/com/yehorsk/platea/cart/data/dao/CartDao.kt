package com.yehorsk.platea.cart.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.yehorsk.platea.cart.data.db.model.CartItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {

    @Query("SELECT * FROM cart_table")
    fun getAllItems() : Flow<List<CartItemEntity>>

    @Transaction
    @Query("SELECT * FROM cart_table")
    suspend fun getAllItemsOnce(): List<CartItemEntity>

    @Query("SELECT COUNT(*) FROM cart_table")
    fun getAmountOfItems(): Flow<Int>

    @Delete
    suspend fun deleteItem(item: CartItemEntity)

    @Delete
    suspend fun deleteItems(items: List<CartItemEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItems(items: List<CartItemEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: CartItemEntity)

    @Update
    suspend fun updateItem(item: CartItemEntity)

    @Query("UPDATE cart_table SET price = :itemPrice, pivot_price = :totalPrice WHERE menu_item_id = :id")
    suspend fun updateCartItemPrice(id: String, itemPrice: String, totalPrice: String)

    @Query("DELETE FROM cart_table WHERE id = :id")
    suspend fun deleteCartItem(id: String)

    @Transaction
    suspend fun runInTransaction(block: suspend () -> Unit) {
        block()
    }

}