package com.example.mobile.ui.screens.client.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile.menu.data.model.Menu
import com.example.mobile.menu.data.model.MenuItem
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
class CleintMainScreenViewModel @Inject constructor(
    val menuRepositoryImpl: MenuRepositoryImpl,
    val connectivityRepository: ConnectivityRepository
) : ViewModel(){

    private val _uiState = MutableStateFlow(ClientMainUiState())
    val uiState: StateFlow<ClientMainUiState> = _uiState.asStateFlow()
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        val isOnline = connectivityRepository.isInternetConnected()
        if(isOnline){
            getMenus()
        }
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

    fun setMenu(menu: MenuItem){
        _uiState.update {
            it.copy(currentMenu = menu)
        }
    }


}

data class ClientMainUiState(
    val menus: List<Menu>? = null,
    val currentMenu: MenuItem? = null
)