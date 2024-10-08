package com.example.mobile.cart.presentation.cart.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile.cart.data.remote.CartRepositoryImpl
import com.example.mobile.core.data.remote.model.NetworkResult
import com.example.mobile.core.domain.repository.SideEffect
import com.example.mobile.menu.data.remote.model.MenuItem
import com.example.mobile.cart.data.remote.model.CartItem
import com.example.mobile.core.presentation.components.CartForm
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CartScreenViewModel @Inject constructor(
    val cartRepositoryImpl: CartRepositoryImpl
) : ViewModel(){

    private val _uiState = MutableStateFlow(CartScreenUiState())
    val uiState: StateFlow<CartScreenUiState> = _uiState.asStateFlow()
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _cartForm = MutableStateFlow(CartForm())
    val cartForm: StateFlow<CartForm> = _cartForm.asStateFlow()

    private val _sideEffectChannel = Channel<SideEffect>(capacity = Channel.BUFFERED)
    val sideEffectFlow: Flow<SideEffect>
        get() = _sideEffectChannel.receiveAsFlow()

    init {
        getItems()
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

    fun getItems(){
        viewModelScope.launch {
            _isLoading.value = true
            _uiState.update { state ->
                when(val result = cartRepositoryImpl.getAllItems()){
                    is NetworkResult.Error -> {
                        if(result.code == 503){
                            state.copy(internetError = true)
                        }else{
                            state.copy(error = result.message.toString())
                        }
                    }
                    is NetworkResult.Success -> state.copy(items = result.data)
                }

            }
            _isLoading.value = false
        }
    }

    fun setItem(item: CartItem){
        _cartForm.update {
            Timber.d("Item $item")
            it.copy(
                quantity = item.pivot.quantity.toInt(),
                price = item.pivot.price.toDouble(),
                note = item.pivot.note,
                menuItemId = item.pivot.menuItemId
            )
        }
    }

    fun updateItem(){
        viewModelScope.launch {
            when(val result = cartRepositoryImpl.updateUserCartItem(_cartForm.value)){
                is NetworkResult.Error -> {
                    if(result.code == 503){
                        _sideEffectChannel.send(SideEffect.ShowToast("No internet connection!"))
                    }else{
                        _sideEffectChannel.send(SideEffect.ShowToast("Unknown error"))
                    }
                }
                is NetworkResult.Success -> {
                    _sideEffectChannel.send(SideEffect.ShowToast(result.data))
                }
            }
        }
        clearForm()
    }

    fun deleteItem(){
        viewModelScope.launch {
            when(val result = cartRepositoryImpl.deleteUserCartItem(_cartForm.value)){
                is NetworkResult.Error -> {
                    if(result.code == 503){
                        _sideEffectChannel.send(SideEffect.ShowToast("No internet connection!"))
                    }else{
                        _sideEffectChannel.send(SideEffect.ShowToast("Unknown error"))
                    }
                }
                is NetworkResult.Success -> {
                    _sideEffectChannel.send(SideEffect.ShowToast(result.data))
                }
            }
        }
        clearForm()
    }

    fun setMenu(menu: MenuItem){
        _uiState.update {
            it.copy(currentMenu = menu)
        }
    }

}

data class CartScreenUiState(
    val items: List<CartItem>? = null,
    val internetError: Boolean = false,
    val error: String = "",
    val currentMenu: MenuItem? = null
)