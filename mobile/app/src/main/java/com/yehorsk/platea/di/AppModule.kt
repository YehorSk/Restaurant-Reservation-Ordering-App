package com.yehorsk.platea.di

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.yehorsk.platea.BuildConfig
import com.yehorsk.platea.auth.data.remote.AuthInterceptor
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
import com.yehorsk.platea.core.utils.ConnectivityObserver
import com.yehorsk.platea.core.utils.NetworkConnectivityObserver
import com.yehorsk.platea.menu.data.dao.MenuDao
import com.yehorsk.platea.menu.data.remote.MenuRepositoryImpl
import com.yehorsk.platea.menu.domain.repository.MenuRepository
import com.yehorsk.platea.menu.domain.service.MenuService
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
    fun providesAuthPreferences(@ApplicationContext applicationContext: Context) : MainPreferencesRepository = MainPreferencesRepository(dataStore = applicationContext.dataStore)

}