package com.example.mobile.cart.presentation.cart.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile.cart.data.remote.CartRepositoryImpl
import com.example.mobile.core.data.remote.dto.NetworkResult
import com.example.mobile.core.data.repository.SideEffect
import com.example.mobile.cart.data.dao.CartDao
import com.example.mobile.cart.data.db.model.CartItemEntity
import com.example.mobile.cart.data.remote.dto.toCartItemEntity
import com.example.mobile.core.presentation.components.CartForm
import com.example.mobile.menu.data.db.model.MenuItemEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CartScreenViewModel @Inject constructor(
    val cartRepositoryImpl: CartRepositoryImpl,
    val cartDao: CartDao
) : ViewModel(){

    private val _uiState = MutableStateFlow(CartScreenUiState())
    val uiState: StateFlow<CartScreenUiState> = _uiState.asStateFlow()

    val cartItemUiState: StateFlow<List<CartItemEntity>> = cartDao.getAllItems()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = listOf()
        )

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

    fun updatePrice(price: Double){
        val formattedPrice = String.format("%.2f", price).toDouble()
        _cartForm.update {
            it.copy(price = formattedPrice)
        }
    }

    fun updateQuantity(quantity: Int){
        _cartForm.update {
            it.copy(quantity = quantity)
        }
    }

    fun clearForm(){
        _cartForm.update {
            it.copy(
                price = 0.00,
                quantity = 1,
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
                    is NetworkResult.Success -> {
                        val serverItemIds = result.data.map { it.id }.toSet()
                        val itemsToDelete = cartItemUiState.value.filter { it.id !in serverItemIds }
                        cartDao.runInTransaction {
                            cartDao.insertItems(result.data.map { it.toCartItemEntity() })
                            cartDao.deleteItems(itemsToDelete)
                        }
                        state.copy(
                            internetError = false
                        )
                    }
                }
            }
            _isLoading.value = false
        }
    }

    fun setItem(item: CartItemEntity){
        _uiState.update {
            it.copy(cartItem = item)
        }
        _cartForm.update {
            Timber.d("Item $item")
            it.copy(
                pivotId = item.pivot.id,
                quantity = item.pivot.quantity,
                price = item.pivot.price,
                menuItemId = item.pivot.menuItemId,
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
                    _sideEffectChannel.send(SideEffect.ShowToast(result.message?:""))
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
                        _sideEffectChannel.send(SideEffect.ShowToast("No internet connection!", code = 503))
                    }else{
                        _sideEffectChannel.send(SideEffect.ShowToast("Unknown error", code = 400))
                    }
                }
                is NetworkResult.Success -> {
                    _sideEffectChannel.send(SideEffect.ShowToast(result.message?:"", code = 200))
                }
            }
        }
        clearForm()
    }

    fun setMenuItem(menu: MenuItemEntity){
        _uiState.update {
            it.copy(currentItem = menu)
        }
    }

}

data class CartScreenUiState(
    val internetError: Boolean = false,
    val error: String = "",
    val currentItem: MenuItemEntity? = null,
    val cartItem: CartItemEntity? = null
)