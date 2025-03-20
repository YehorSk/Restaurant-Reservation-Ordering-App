package com.yehorsk.platea.core.di

import com.yehorsk.platea.auth.data.remote.service.AuthService
import com.yehorsk.platea.cart.data.remote.service.CartService
import com.yehorsk.platea.core.data.remote.service.ProfileService
import com.yehorsk.platea.menu.data.remote.service.MenuService
import com.yehorsk.platea.orders.data.remote.service.OrderService
import com.yehorsk.platea.reservations.data.remote.service.ReservationService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    @Provides
    @Singleton
    fun provideAuthApiService(retrofit: Retrofit): AuthService = retrofit.create(AuthService::class.java)

    @Provides
    @Singleton
    fun provideCartApiService(retrofit: Retrofit): CartService = retrofit.create(CartService::class.java)

    @Provides
    @Singleton
    fun provideMenuApiService(retrofit: Retrofit): MenuService = retrofit.create(MenuService::class.java)

    @Provides
    @Singleton
    fun provideOrderApiService(retrofit: Retrofit): OrderService = retrofit.create(OrderService::class.java)

    @Provides
    @Singleton
    fun provideProfileApiService(retrofit: Retrofit): ProfileService = retrofit.create(ProfileService::class.java)

    @Provides
    @Singleton
    fun provideReservationApiService(retrofit: Retrofit): ReservationService = retrofit.create(ReservationService::class.java)

}