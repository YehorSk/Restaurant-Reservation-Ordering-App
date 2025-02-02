package com.yehorsk.platea.di

import com.yehorsk.platea.auth.data.remote.AuthRepositoryImpl
import com.yehorsk.platea.auth.data.repository.AuthRepository
import com.yehorsk.platea.auth.data.service.AuthService
import com.yehorsk.platea.cart.data.dao.CartDao
import com.yehorsk.platea.cart.data.remote.CartRepositoryImpl
import com.yehorsk.platea.cart.domain.repository.CartRepository
import com.yehorsk.platea.cart.domain.service.CartService
import com.yehorsk.platea.core.data.db.MainRoomDatabase
import com.yehorsk.platea.core.data.repository.MainPreferencesRepository
import com.yehorsk.platea.core.data.repository.ProfileRepositoryImpl
import com.yehorsk.platea.core.domain.remote.ProfileService
import com.yehorsk.platea.core.domain.repository.ProfileRepository
import com.yehorsk.platea.menu.data.dao.MenuDao
import com.yehorsk.platea.menu.data.remote.MenuRepositoryImpl
import com.yehorsk.platea.menu.domain.repository.MenuRepository
import com.yehorsk.platea.menu.domain.service.MenuService
import com.yehorsk.platea.notifications.data.dao.NotificationDao
import com.yehorsk.platea.notifications.data.remote.NotificationRepositoryImpl
import com.yehorsk.platea.notifications.domain.repository.NotificationRepository
import com.yehorsk.platea.notifications.domain.service.NotificationService
import com.yehorsk.platea.orders.data.dao.OrderDao
import com.yehorsk.platea.orders.data.remote.GooglePlacesRepositoryImpl
import com.yehorsk.platea.orders.data.remote.OrderRepositoryImpl
import com.yehorsk.platea.orders.domain.repository.GooglePlacesRepository
import com.yehorsk.platea.orders.domain.repository.OrderRepository
import com.yehorsk.platea.orders.domain.service.GooglePlacesApi
import com.yehorsk.platea.orders.domain.service.OrderService
import com.yehorsk.platea.reservations.data.dao.ReservationDao
import com.yehorsk.platea.reservations.data.remote.ReservationRepositoryImpl
import com.yehorsk.platea.reservations.domain.repository.ReservationRepository
import com.yehorsk.platea.reservations.domain.service.ReservationService
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
    fun provideProfileRepositoryImpl(profileService: ProfileService, mainPreferencesRepository: MainPreferencesRepository) : ProfileRepository = ProfileRepositoryImpl(profileService, mainPreferencesRepository)

    @Provides
    @Singleton
    fun providesOrderRepositoryImpl(orderService: OrderService, orderDao: OrderDao, cartDao: CartDao) : OrderRepository = OrderRepositoryImpl(orderService, orderDao, cartDao)

    @Provides
    @Singleton
    fun providesReservationRepositoryImpl(reservationDao: ReservationDao, reservationService: ReservationService) : ReservationRepository = ReservationRepositoryImpl(reservationDao, reservationService)

    @Provides
    @Singleton
    fun providesNotificationRepositoryImpl(notificationDao: NotificationDao, notificationService: NotificationService) : NotificationRepository = NotificationRepositoryImpl(notificationService, notificationDao)

    @Provides
    @Singleton
    fun provideGooglePlacesRepositoryImpl(googlePlacesApi: GooglePlacesApi) : GooglePlacesRepository = GooglePlacesRepositoryImpl(googlePlacesApi)

}