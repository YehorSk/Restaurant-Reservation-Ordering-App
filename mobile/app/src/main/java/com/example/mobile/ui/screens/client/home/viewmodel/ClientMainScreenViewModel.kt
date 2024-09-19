package com.example.mobile.ui.screens.client.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile.menu.data.model.Menu
import com.example.mobile.menu.data.repository.MenuRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CleintMainScreenViewModel @Inject constructor(
    val menuRepositoryImpl: MenuRepositoryImpl
) : ViewModel(){

    private val _uiState = MutableStateFlow(ClientMainUiState())
    val uiState: StateFlow<ClientMainUiState> = _uiState.asStateFlow()
    private val _isLoading = MutableStateFlow(false)
    val isLoadind = _isLoading.asStateFlow()

    init {
        getMenus()
    }

    fun getMenus(){
        viewModelScope.launch {
            _isLoading.value = true
            _uiState.update { state ->
                state.copy(menus = menuRepositoryImpl.getAllMenus())
            }
            _isLoading.value = false
        }
    }


}

data class ClientMainUiState(
    val menus: List<Menu>? = null
)