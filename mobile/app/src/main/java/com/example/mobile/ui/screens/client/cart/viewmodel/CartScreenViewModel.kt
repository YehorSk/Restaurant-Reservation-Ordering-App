package com.example.mobile.ui.screens.client.cart.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile.menu.data.model.MenuItemUser
import com.example.mobile.menu.data.repository.MenuRepositoryImpl
import com.example.mobile.utils.ConnectivityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartScreenViewModel @Inject constructor(
    val menuRepositoryImpl: MenuRepositoryImpl,
    val connectivityRepository: ConnectivityRepository
) : ViewModel(){

    private val _uiState = MutableStateFlow(CartScreenUiState())
    val uiState: StateFlow<CartScreenUiState> = _uiState.asStateFlow()
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        val isOnline = connectivityRepository.isInternetConnected()
        if(isOnline){
            getItems()
        }
    }

    fun getItems(){
        viewModelScope.launch {
            _isLoading.value = true
            _uiState.update { state ->
                state.copy(items = menuRepositoryImpl.getAllItems())
            }
            _isLoading.value = false
        }
    }

}

data class CartScreenUiState(
    val items: List<MenuItemUser>? = null
)