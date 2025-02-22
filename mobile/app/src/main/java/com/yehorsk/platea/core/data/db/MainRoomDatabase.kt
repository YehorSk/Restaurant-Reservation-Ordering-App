package com.yehorsk.platea.core.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.yehorsk.platea.cart.data.dao.CartDao
import com.yehorsk.platea.cart.data.db.model.CartItemEntity
import com.yehorsk.platea.menu.data.dao.MenuDao
import com.yehorsk.platea.menu.data.db.model.MenuEntity
import com.yehorsk.platea.menu.data.db.model.MenuItemEntity
import com.yehorsk.platea.orders.data.dao.OrderDao
import com.yehorsk.platea.orders.data.db.model.OrderEntity
import com.yehorsk.platea.orders.data.db.model.OrderItemEntity
import com.yehorsk.platea.reservations.data.dao.ReservationDao
import com.yehorsk.platea.reservations.data.db.model.ReservationEntity

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