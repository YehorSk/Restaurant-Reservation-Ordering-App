package com.yehorsk.platea.core.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yehorsk.platea.cart.domain.repository.CartRepository
import com.yehorsk.platea.core.data.repository.MainPreferencesRepository
import com.yehorsk.platea.core.domain.repository.RestaurantRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class MainScreenViewModel @Inject constructor(
    val preferences: MainPreferencesRepository,
    val cartRepository: CartRepository,
    val restaurantRepository: RestaurantRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ThemeState(isDarkMode = false, language = "en"))
    val uiState: StateFlow<ThemeState> = _uiState

    val cartItemCount: StateFlow<Int> = cartRepository.getAmountOfItems()
        .stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            0
        )

    init {
        refreshData()
        observeTheme()
        observeLanguage()
    }

    fun refreshData() {
        Timber.d("refreshData")
        viewModelScope.launch {
            cartRepository.getAllItems()
            restaurantRepository.getRestaurantInfo()
        }
    }

    private fun observeTheme(){
        preferences.appIsDarkThemeFlow
            .onEach { theme ->
                _uiState.update {
                    it.copy(isDarkMode = theme ?: false)
                }
            }
            .launchIn(viewModelScope)
    }

    private fun observeLanguage(){
        preferences.appLanguageFlow
            .onEach { lang ->
                _uiState.update {
                    it.copy(language = lang ?: "en")
                }
            }
            .launchIn(viewModelScope)
    }

}