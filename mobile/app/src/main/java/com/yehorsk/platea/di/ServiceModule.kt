package com.yehorsk.platea.di

import com.yehorsk.platea.auth.data.service.AuthService
import com.yehorsk.platea.cart.domain.service.CartService
import com.yehorsk.platea.core.domain.remote.ProfileService
import com.yehorsk.platea.menu.domain.service.MenuService
import com.yehorsk.platea.notifications.domain.service.NotificationService
import com.yehorsk.platea.orders.domain.service.GooglePlacesApi
import com.yehorsk.platea.orders.domain.service.OrderService
import com.yehorsk.platea.reservations.domain.service.ReservationService
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

    @Provides
    @Singleton
    fun provideNotificationApiService(retrofit: Retrofit): NotificationService = retrofit.create(NotificationService::class.java)

    @Provides
    @Singleton
    fun provideGooglePlacesApi(retrofit: Retrofit): GooglePlacesApi = retrofit.create(GooglePlacesApi::class.java)
}