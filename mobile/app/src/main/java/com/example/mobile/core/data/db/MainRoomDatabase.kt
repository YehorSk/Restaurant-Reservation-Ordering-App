package com.example.mobile.core.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mobile.cart.data.db.model.CartItemEntity
import com.example.mobile.cart.data.dao.CartDao
import com.example.mobile.menu.data.dao.MenuDao
import com.example.mobile.menu.data.db.model.MenuEntity
import com.example.mobile.menu.data.db.model.MenuItemEntity

@Database(
    entities = [
        CartItemEntity::class,
        MenuItemEntity::class,
        MenuEntity::class
    ],
    version = 1
)
abstract class MainRoomDatabase : RoomDatabase() {

    abstract val cartDao: CartDao
    abstract val menuDao: MenuDao

}