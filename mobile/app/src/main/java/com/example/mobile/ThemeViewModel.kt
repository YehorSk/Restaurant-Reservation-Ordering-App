package com.example.mobile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile.core.data.repository.MainPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
    val preferences: MainPreferencesRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(ThemeState(isDarkMode = false))
    val uiState: StateFlow<ThemeState> = _uiState

    init {
        viewModelScope.launch(Dispatchers.IO){
            preferences.appIsDarkThemeFlow.collect{ theme ->
                _uiState.update {
                    it.copy(isDarkMode = theme ?: false)
                }
            }
        }
    }

}