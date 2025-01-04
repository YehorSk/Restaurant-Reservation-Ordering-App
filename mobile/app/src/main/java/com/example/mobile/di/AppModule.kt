package com.example.mobile.di

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.example.mobile.BuildConfig
import com.example.mobile.auth.data.remote.AuthInterceptor
import com.example.mobile.core.data.repository.MainPreferencesRepository
import com.example.mobile.auth.data.repository.AuthRepository
import com.example.mobile.auth.data.remote.AuthRepositoryImpl
import com.example.mobile.auth.data.service.AuthService
import com.example.mobile.cart.data.remote.CartRepositoryImpl
import com.example.mobile.cart.data.dao.CartDao
import com.example.mobile.cart.domain.repository.CartRepository
import com.example.mobile.cart.domain.service.CartService
import com.example.mobile.core.data.db.MainRoomDatabase
import com.example.mobile.core.data.repository.ProfileRepositoryImpl
import com.example.mobile.core.domain.remote.ProfileService
import com.example.mobile.core.domain.repository.ProfileRepository
import com.example.mobile.menu.data.dao.MenuDao
import com.example.mobile.menu.data.remote.MenuRepositoryImpl
import com.example.mobile.menu.domain.repository.MenuRepository
import com.example.mobile.menu.domain.service.MenuService
import com.example.mobile.orders.data.remote.OrderRepositoryImpl
import com.example.mobile.orders.domain.repository.OrderRepository
import com.example.mobile.orders.domain.service.OrderService
import com.example.mobile.core.utils.ConnectivityObserver
import com.example.mobile.core.utils.ConnectivityRepository
import com.example.mobile.core.utils.NetworkConnectivityObserver
import com.example.mobile.orders.data.dao.OrderDao
import com.example.mobile.reservations.data.dao.ReservationDao
import com.example.mobile.reservations.data.remote.ReservationRepositoryImpl
import com.example.mobile.reservations.domain.repository.ReservationRepository
import com.example.mobile.reservations.domain.service.ReservationService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

private const val AUTH_PREFERENCE_NAME = "auth_preferences"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = AUTH_PREFERENCE_NAME
)

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideBaseUrl():  String = BuildConfig.BASE_URL

    @Provides
    @Singleton
    fun provideAuthInterceptor(mainPreferencesRepository: MainPreferencesRepository): AuthInterceptor {
        return AuthInterceptor(mainPreferencesRepository)
    }

    @Provides
    fun providesClient(authInterceptor: AuthInterceptor): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        encodeDefaults = true
    }

    @Provides
    @Singleton
    fun provideRetrofit(baseUrl: String, client: OkHttpClient) : Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(client)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    @Provides
    @Singleton
    fun myRoomDatabase(application: Application): MainRoomDatabase {
        return Room.databaseBuilder(
            application,
            MainRoomDatabase::class.java,
            "MobileDatabase"
        ).build()
    }

    @Provides
    @Singleton
    fun provideNetworkConnectivityObserver(@ApplicationContext context: Context): ConnectivityObserver = NetworkConnectivityObserver(context)

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
    fun providesAuthPreferences(@ApplicationContext applicationContext: Context) : MainPreferencesRepository = MainPreferencesRepository(dataStore = applicationContext.dataStore)

    @Provides
    @Singleton
    fun connectivityRepository(@ApplicationContext applicationContext: Context) : ConnectivityRepository = ConnectivityRepository(context = applicationContext)

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
}