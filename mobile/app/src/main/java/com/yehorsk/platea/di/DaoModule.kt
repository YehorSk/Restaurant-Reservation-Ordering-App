package com.yehorsk.platea.di

import com.yehorsk.platea.cart.data.dao.CartDao
import com.yehorsk.platea.core.data.db.MainRoomDatabase
import com.yehorsk.platea.menu.data.dao.MenuDao
import com.yehorsk.platea.notifications.data.dao.NotificationDao
import com.yehorsk.platea.orders.data.dao.OrderDao
import com.yehorsk.platea.reservations.data.dao.ReservationDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {

    @Provides
    @Singleton
    fun provideCartDao(database: MainRoomDatabase): CartDao = database.cartDao

    @Provides
    @Singleton
    fun provideMenuDao(database: MainRoomDatabase): MenuDao = database.menuDao

    @Provides
    @Singleton
    fun provideOrderDao(database: MainRoomDatabase): OrderDao = database.orderDao

    @Provides
    @Singleton
    fun provideReservationDao(database: MainRoomDatabase): ReservationDao = database.reservationDao

    @Provides
    @Singleton
    fun provideNotificationDao(database: MainRoomDatabase): NotificationDao = database.notificationDao

}