package com.yehorsk.platea.core.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.yehorsk.platea.auth.data.remote.model.AuthDataDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MainPreferencesRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
){
    private companion object {
        val USER_NAME = stringPreferencesKey("USER_NAME")
        val USER_EMAIL = stringPreferencesKey("USER_EMAIL")
        val JWT_TOKEN = stringPreferencesKey("JWT_TOKEN")
        val USER_ROLE = stringPreferencesKey("USER_ROLE")
        val USER_PHONE = stringPreferencesKey("USER_PHONE")
        val USER_ADDRESS = stringPreferencesKey("USER_ADDRESS")
        val APP_LANGUAGE = stringPreferencesKey("APP_LANGUAGE")
        val APP_THEME = booleanPreferencesKey("APP_THEME")
        val FCM_TOKEN = stringPreferencesKey("FCM_TOKEN")
    }

    val userNameFlow: Flow<String?> = dataStore.data
        .map { preferences -> preferences[USER_NAME]}

    val userEmailFlow: Flow<String?> = dataStore.data
        .map { preferences -> preferences[USER_EMAIL] }

    val userRoleFlow: Flow<String?> = dataStore.data
        .map { preferences -> preferences[USER_ROLE] }

    val userPhoneFlow: Flow<String?> = dataStore.data
        .map { preferences -> preferences[USER_PHONE] }

    val userAddressFlow: Flow<String?> = dataStore.data
        .map { preferences -> preferences[USER_ADDRESS] }

    val jwtTokenFlow: Flow<String?> = dataStore.data
        .map { preferences -> preferences[JWT_TOKEN] }

    val fcmTokenFlow: Flow<String?> = dataStore.data
        .map { preferences -> preferences[FCM_TOKEN] }

    val appLanguageFlow: Flow<String?> = dataStore.data.map { preferences ->
        preferences[APP_LANGUAGE] ?: "en"
    }

    val appIsDarkThemeFlow: Flow<Boolean?> = dataStore.data.map { preferences ->
        preferences[APP_THEME] ?: false
    }

    suspend fun saveUserName(userName: String) {
        dataStore.edit { preferences ->
            preferences[USER_NAME] = userName
        }
    }

    suspend fun saveUserEmail(userEmail: String) {
        dataStore.edit { preferences ->
            preferences[USER_EMAIL] = userEmail
        }
    }

    suspend fun saveUserRole(userRole: String) {
        dataStore.edit { preferences ->
            preferences[USER_ROLE] = userRole
        }
    }

    suspend fun saveUserPhone(userPhone: String) {
        dataStore.edit { preferences ->
            preferences[USER_PHONE] = userPhone
        }
    }

    suspend fun saveUserAddress(userAddress: String) {
        dataStore.edit { preferences ->
            preferences[USER_ADDRESS] = userAddress
        }
    }

    suspend fun saveJwtToken(jwtToken: String) {
        dataStore.edit { preferences ->
            preferences[JWT_TOKEN] = jwtToken
        }
    }

    suspend fun setAppLanguage(language: String) {
        dataStore.edit { preferences ->
            preferences[APP_LANGUAGE] = language
        }
    }

    suspend fun setAppTheme(theme: Boolean) {
        dataStore.edit { preferences -> preferences[APP_THEME] = theme }
    }

    suspend fun saveUser(authData: AuthDataDto){
        saveJwtToken(authData.token ?: "")
        saveUserRole(authData.user?.role ?: "")
        saveUserName(authData.user?.name ?: "")
        saveUserEmail(authData.user?.email ?: "")
        saveUserPhone(authData.user?.phone ?: "")
        saveUserAddress(authData.user?.address ?: "")
    }

    suspend fun clearAllTokens(){
        dataStore.edit { preferences ->
            preferences.remove(USER_NAME)
            preferences.remove(USER_EMAIL)
            preferences.remove(USER_ROLE)
            preferences.remove(USER_PHONE)
            preferences.remove(USER_ADDRESS)
            preferences.remove(JWT_TOKEN)
        }
    }
}