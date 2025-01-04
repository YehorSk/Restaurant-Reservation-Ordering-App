package com.example.mobile.core.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.mobile.auth.data.remote.model.AuthDataDto
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

    suspend fun saveUser(authData: AuthDataDto){
        saveUserName(authData.user?.name ?: "")
        saveUserEmail(authData.user?.email ?: "")
        saveUserRole(authData.user?.role ?: "")
        saveUserPhone(authData.user?.phone ?: "")
        saveUserAddress(authData.user?.address ?: "")
        saveJwtToken(authData.token ?: "")
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