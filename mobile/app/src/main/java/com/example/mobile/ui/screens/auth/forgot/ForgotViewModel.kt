package com.example.mobile.ui.screens.auth.forgot

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import com.example.mobile.auth.data.repository.AuthPreferencesRepository
import com.example.mobile.auth.data.repository.AuthRepository
import com.example.mobile.ui.screens.auth.login.LoginState
import com.example.mobile.utils.ConnectivityRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class ForgotViewModel @Inject constructor(
    val authRepository: AuthRepository,
    val preferencesRepository: AuthPreferencesRepository,
    val connectivityRepository: ConnectivityRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(LoginState())
    val uiState: StateFlow<LoginState> = _uiState.asStateFlow()



}