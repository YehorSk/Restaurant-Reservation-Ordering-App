package com.example.mobile.ui.screens.common.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile.auth.data.model.AuthResult
import com.example.mobile.auth.data.model.HttpResponse
import com.example.mobile.auth.data.repository.AuthPreferencesRepository
import com.example.mobile.auth.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    val authRepository: AuthRepository,
    val preferencesRepository: AuthPreferencesRepository
): ViewModel() {

    val _uiState = MutableStateFlow(SettingsState())
    val uiState: StateFlow<SettingsState> = _uiState.asStateFlow()

    fun logout(){
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    isLoading = true
                )
            }
            authRepository.logout()
            preferencesRepository.clearAllTokens()
            _uiState.update { currentState ->
                currentState.copy(
                    isLoading = false,
                    isLoggedOut = true
                )
            }

        }
    }
}