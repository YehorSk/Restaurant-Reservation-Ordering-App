package com.example.mobile.ui.screens.client.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile.common.NetworkResult
import com.example.mobile.menu.data.model.Menu
import com.example.mobile.menu.data.model.MenuItem
import com.example.mobile.menu.data.repository.MenuRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import javax.inject.Inject

@HiltViewModel
class ClientMainScreenViewModel @Inject constructor(
    val menuRepositoryImpl: MenuRepositoryImpl
) : ViewModel(){

    private val _uiState = MutableStateFlow(ClientMainUiState())
    val uiState: StateFlow<ClientMainUiState> = _uiState.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _cartForm = MutableStateFlow(CartForm())
    val cartForm: StateFlow<CartForm> = _cartForm.asStateFlow()

    init {
        getMenus()
    }

    fun updateNote(note: String){
        _cartForm.update {
            it.copy(note = note)
        }
    }

    fun updatePrice(price: Double){
        _cartForm.update {
            it.copy(price = price)
        }
    }

    fun updateQuantity(quantity: Int){
        _cartForm.update {
            it.copy(quantity = quantity)
        }
    }

    fun setMenuItemId(id: String){
        _cartForm.update {
            it.copy(
                menuItemId = id
            )
        }
    }

    fun clearForm(){
        _cartForm.update {
            it.copy(
                price = 0.00,
                quantity = 1,
                note = "",
                menuItemId = ""
            )
        }
    }

    fun addUserCartItem(){
        clearForm()
        viewModelScope.launch {
            _isLoading.value = true
            _uiState.update { state ->
                when(val result = menuRepositoryImpl.addUserCartItem(cartForm = cartForm.value)){
                    is NetworkResult.Error -> {
                        if(result.code == 503){
                            state.copy(
                                internetError = true,
                                success = ""
                            )
                        }else{
                            state.copy(
                                error = result.message.toString(),
                                success = ""
                            )
                        }
                    }
                    is NetworkResult.Success -> state.copy(
                        success = result.data
                    )
                }
            }
            _isLoading.value = false
        }
    }

    fun getMenus(){
        viewModelScope.launch {
            _isLoading.value = true
            _uiState.update { state ->
                when(val result = menuRepositoryImpl.getAllMenus()){
                    is NetworkResult.Error -> {
                        if(result.code == 503){
                            state.copy(
                                internetError = true,
                                success = ""
                            )
                        }else{
                            state.copy(
                                error = result.message.toString(),
                                success = ""
                            )
                        }
                    }
                    is NetworkResult.Success -> state.copy(
                        menus = result.data,
                        internetError = false,
                        error = ""
                    )
                }
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
    val currentMenu: MenuItem? = null,
    val internetError: Boolean = false,
    val error: String = "",
    val success: String = "",
)

@Serializable
data class CartForm(
    val quantity: Int = 1,
    val price: Double = 0.00,
    val note: String = "",
    @SerialName("menu_item_id")
    val menuItemId: String = ""
)