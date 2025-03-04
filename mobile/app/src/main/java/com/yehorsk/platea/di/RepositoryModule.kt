package com.yehorsk.platea.di

import com.yehorsk.platea.auth.data.remote.AuthRepositoryImpl
import com.yehorsk.platea.auth.domain.repository.AuthRepository
import com.yehorsk.platea.auth.data.remote.service.AuthService
import com.yehorsk.platea.cart.data.dao.CartDao
import com.yehorsk.platea.cart.data.remote.CartRepositoryImpl
import com.yehorsk.platea.cart.domain.repository.CartRepository
import com.yehorsk.platea.cart.data.remote.service.CartService
import com.yehorsk.platea.core.data.dao.RestaurantInfoDao
import com.yehorsk.platea.core.data.db.MainRoomDatabase
import com.yehorsk.platea.core.data.repository.MainPreferencesRepository
import com.yehorsk.platea.core.data.repository.ProfileRepositoryImpl
import com.yehorsk.platea.core.data.remote.service.ProfileService
import com.yehorsk.platea.core.domain.repository.ProfileRepository
import com.yehorsk.platea.menu.data.dao.MenuDao
import com.yehorsk.platea.menu.data.remote.MenuRepositoryImpl
import com.yehorsk.platea.menu.domain.repository.MenuRepository
import com.yehorsk.platea.menu.data.remote.service.MenuService
import com.yehorsk.platea.orders.data.dao.OrderDao
import com.yehorsk.platea.orders.data.remote.OrderRepositoryImpl
import com.yehorsk.platea.orders.domain.repository.OrderRepository
import com.yehorsk.platea.orders.data.remote.service.OrderService
import com.yehorsk.platea.reservations.data.dao.ReservationDao
import com.yehorsk.platea.reservations.data.remote.ReservationRepositoryImpl
import com.yehorsk.platea.reservations.domain.repository.ReservationRepository
import com.yehorsk.platea.reservations.data.remote.service.ReservationService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideAuthRepositoryImpl(authService: AuthService, mainPreferencesRepository: MainPreferencesRepository, mainRoomDatabase: MainRoomDatabase) : AuthRepository = AuthRepositoryImpl(authService, mainPreferencesRepository, mainRoomDatabase)

    @Provides
    @Singleton
    fun provideCartRepositoryImpl(cartService: CartService, cartDao: CartDao) : CartRepository = CartRepositoryImpl(cartService, cartDao)

    @Provides
    @Singleton
    fun provideMenuRepositoryImpl(menuService: MenuService, menuDao: MenuDao) : MenuRepository = MenuRepositoryImpl(menuService, menuDao)

    @Provides
    @Singleton
    fun provideProfileRepositoryImpl(profileService: ProfileService, mainPreferencesRepository: MainPreferencesRepository, mainRoomDatabase: MainRoomDatabase, restaurantInfoDao: RestaurantInfoDao) : ProfileRepository = ProfileRepositoryImpl(profileService, mainPreferencesRepository, mainRoomDatabase, restaurantInfoDao)

    @Provides
    @Singleton
    fun providesOrderRepositoryImpl(orderService: OrderService, orderDao: OrderDao, cartDao: CartDao) : OrderRepository = OrderRepositoryImpl(orderService, orderDao, cartDao)

    @Provides
    @Singleton
    fun providesReservationRepositoryImpl(reservationDao: ReservationDao, orderDao: OrderDao, reservationService: ReservationService) : ReservationRepository = ReservationRepositoryImpl(reservationDao, orderDao, reservationService)

}