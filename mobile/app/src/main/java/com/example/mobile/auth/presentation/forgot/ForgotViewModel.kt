package com.example.mobile.auth.presentation.forgot

import androidx.lifecycle.ViewModel
import com.example.mobile.auth.data.remote.AuthPreferencesRepository
import com.example.mobile.auth.data.repository.AuthRepository
import com.example.mobile.auth.presentation.login.LoginState
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