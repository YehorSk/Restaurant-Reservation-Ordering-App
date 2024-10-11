package com.example.mobile.core.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mobile.cart.data.db.model.CartItemEntity
import com.example.mobile.cart.data.dao.CartDao

@Database(
    entities = [
        CartItemEntity::class
    ],
    version = 1
)
abstract class MainRoomDatabase : RoomDatabase() {

    abstract val cartDao: CartDao

}