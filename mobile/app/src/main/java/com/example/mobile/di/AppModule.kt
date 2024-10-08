package com.example.mobile.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.mobile.auth.data.remote.AuthPreferencesRepository
import com.example.mobile.auth.domain.repository.AuthRepository
import com.example.mobile.auth.data.remote.AuthRepositoryImpl
import com.example.mobile.auth.domain.service.AuthService
import com.example.mobile.cart.data.remote.CartRepositoryImpl
import com.example.mobile.cart.domain.repository.CartRepository
import com.example.mobile.cart.domain.service.CartService
import com.example.mobile.core.Constants
import com.example.mobile.menu.data.remote.MenuRepositoryImpl
import com.example.mobile.menu.domain.repository.MenuRepository
import com.example.mobile.menu.domain.service.MenuService
import com.example.mobile.utils.ConnectivityRepository
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

// Home
    @Provides
    fun provideBaseUrl():  String = Constants.BASE_URL

//    @Provides
//    fun provideBaseUrl():  String = "http://192.168.102.5/SavchukBachelor/backend/public/api/"

    @Provides
    fun providesClient(): OkHttpClient = OkHttpClient.Builder()
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
    fun provideAuthApiService(retrofit: Retrofit): AuthService = retrofit.create(AuthService::class.java)

    @Provides
    @Singleton
    fun provideCartApiService(retrofit: Retrofit): CartService = retrofit.create(CartService::class.java)

    @Provides
    @Singleton
    fun provideMenuApiService(retrofit: Retrofit): MenuService = retrofit.create(MenuService::class.java)


    @Provides
    @Singleton
    fun providesAuthPreferences(@ApplicationContext applicationContext: Context) : AuthPreferencesRepository = AuthPreferencesRepository(dataStore = applicationContext.dataStore)

    @Provides
    @Singleton
    fun connectivityRepository(@ApplicationContext applicationContext: Context) : ConnectivityRepository = ConnectivityRepository(context = applicationContext)

    @Provides
    @Singleton
    fun provideAuthRepositoryImpl(authService: AuthService, authPreferencesRepository: AuthPreferencesRepository, connectivityRepository: ConnectivityRepository) : AuthRepository = AuthRepositoryImpl(authService,authPreferencesRepository,connectivityRepository)

    @Provides
    @Singleton
    fun provideCartRepositoryImpl(cartService: CartService, authPreferencesRepository: AuthPreferencesRepository, connectivityRepository: ConnectivityRepository) : CartRepository = CartRepositoryImpl(cartService,authPreferencesRepository,connectivityRepository)

    @Provides
    @Singleton
    fun provideMenuRepositoryImpl(menuService: MenuService, authPreferencesRepository: AuthPreferencesRepository, connectivityRepository: ConnectivityRepository) : MenuRepository = MenuRepositoryImpl(menuService,authPreferencesRepository,connectivityRepository)


}