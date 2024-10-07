package com.example.mobile.ui.screens.client.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile.common.NetworkResult
import com.example.mobile.common.SideEffect
import com.example.mobile.menu.data.model.Menu
import com.example.mobile.menu.data.model.MenuItem
import com.example.mobile.menu.data.repository.MenuRepositoryImpl
import com.example.mobile.ui.screens.common.CartForm
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import timber.log.Timber
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

    private val _sideEffectChannel = Channel<SideEffect>(capacity = Channel.BUFFERED)
    val sideEffectFlow: Flow<SideEffect>
        get() = _sideEffectChannel.receiveAsFlow()

    init {
        getMenus()
    }

    fun updateNote(note: String){
        Timber.d("Note $note")
        Timber.d("Cart ${_cartForm.value}")
        _cartForm.update {
            it.copy(note = note)
        }
    }

    fun updatePrice(price: Double){
        Timber.d("price $price")
        Timber.d("Cart ${_cartForm.value}")
        _cartForm.update {
            it.copy(price = price)
        }
    }

    fun updateQuantity(quantity: Int){
        Timber.d("Quantity $quantity")
        Timber.d("Cart ${_cartForm.value}")
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
        viewModelScope.launch {
            _isLoading.value = true
            _uiState.update { state ->
                Timber.d(_cartForm.value.toString())
                when(val result = menuRepositoryImpl.addUserCartItem(cartForm = _cartForm.value)){
                    is NetworkResult.Error -> {
                        if(result.code == 503){
                            _sideEffectChannel.send(SideEffect.ShowToast("No internet connection!"))
                            state.copy(
                                internetError = true
                            )
                        }else{
                            state.copy()
                        }
                    }
                    is NetworkResult.Success -> {
                        _sideEffectChannel.send(SideEffect.ShowToast(result.data))
                        state.copy()
                    }
                }
            }
            _isLoading.value = false
            clearForm()
        }
    }

    fun getMenus(){
        viewModelScope.launch {
            _isLoading.value = true
            _uiState.update { state ->
                when(val result = menuRepositoryImpl.getAllMenus()){
                    is NetworkResult.Error -> {
                        if(result.code == 503){
                            _sideEffectChannel.send(SideEffect.ShowToast("No internet connection!"))
                            state.copy(
                                internetError = true,
                            )
                        }else{
                            _sideEffectChannel.send(SideEffect.ShowToast(result.message.toString()))
                            state.copy()
                        }
                    }
                    is NetworkResult.Success ->
                        state.copy(
                        menus = result.data,
                        internetError = false
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
)

