package com.example.mobile.core.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mobile.cart.data.db.model.CartItemEntity
import com.example.mobile.cart.data.dao.CartDao
import com.example.mobile.menu.data.dao.MenuDao
import com.example.mobile.menu.data.db.model.MenuEntity
import com.example.mobile.menu.data.db.model.MenuItemEntity
import com.example.mobile.orders.data.dao.OrderDao
import com.example.mobile.orders.data.dao.ReservationDao
import com.example.mobile.orders.data.db.model.OrderEntity
import com.example.mobile.orders.data.db.model.OrderItemEntity
import com.example.mobile.orders.data.db.model.ReservationEntity

@Database(
    entities = [
        CartItemEntity::class,
        MenuItemEntity::class,
        MenuEntity::class,
        OrderEntity::class,
        OrderItemEntity::class,
        ReservationEntity::class
    ],
    version = 1
)
abstract class MainRoomDatabase : RoomDatabase() {

    abstract val cartDao: CartDao
    abstract val menuDao: MenuDao
    abstract val orderDao: OrderDao
    abstract val reservationDao: ReservationDao

}