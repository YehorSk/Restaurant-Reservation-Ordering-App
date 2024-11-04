package com.example.mobile.cart.presentation.cart.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile.cart.data.remote.CartRepositoryImpl
import com.example.mobile.core.data.remote.dto.NetworkResult
import com.example.mobile.core.domain.SideEffect
import com.example.mobile.cart.data.dao.CartDao
import com.example.mobile.cart.data.db.model.CartItemEntity
import com.example.mobile.cart.data.remote.dto.toCartItemEntity
import com.example.mobile.cart.presentation.cart.CartScreenUiState
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

    private val _sideEffectChannel = Channel<SideEffect>(capacity = Channel.BUFFERED)
    val sideEffectFlow: Flow<SideEffect>
        get() = _sideEffectChannel.receiveAsFlow()

    init {
        getItems()
    }

    fun updatePrice(price: Double){
        val formattedPrice = String.format("%.2f", price).toDouble()
        _uiState.update {
            it.copy(cartForm = it.cartForm.copy(price = formattedPrice))
        }
    }

    fun updateQuantity(quantity: Int){
        _uiState.update {
            it.copy(cartForm = it.cartForm.copy(quantity = quantity))
        }
    }

    fun clearForm(){
        _uiState.update {
            it.copy(cartForm = it.cartForm.copy(
                price = 0.00,
                quantity = 1,
                menuItemId = 0
            ))
        }
    }

    fun getItems() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            cartItemUiState.collect { localItems ->
                when (val result = cartRepositoryImpl.getAllItems()) {
                    is NetworkResult.Error -> {
                        _uiState.update {
                            it.copy(
                                internetError = result.code == 503,
                                error = result.message ?: "Unknown error"
                            )
                        }
                    }
                    is NetworkResult.Success -> {
                        Timber.d("Items from server: ${result.data}")
                        val serverItemIds = result.data.map { it.id }.toSet()
                        val itemsToDelete = localItems.filter { it.id !in serverItemIds }

                        cartDao.runInTransaction {
                            cartDao.insertItems(result.data.map { it.toCartItemEntity() })
                            cartDao.deleteItems(itemsToDelete)
                        }

                        _uiState.update {
                            it.copy(internetError = false, error = "")
                        }
                    }
                }

                _uiState.update { it.copy(isLoading = false) }

                return@collect
            }
        }
    }


    fun setItem(item: CartItemEntity){
        _uiState.update {
            it.copy(cartItem = item)
        }
        _uiState.update {
            it.copy(cartForm = it.cartForm.copy(
                pivotId = item.pivot.id,
                quantity = item.pivot.quantity,
                price = item.pivot.price,
                menuItemId = item.pivot.menuItemId,
            ))
        }
    }

    fun updateItem(){
        viewModelScope.launch {
            when(val result = cartRepositoryImpl.updateUserCartItem(_uiState.value.cartForm)){
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
            when(val result = cartRepositoryImpl.deleteUserCartItem(_uiState.value.cartForm)){
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
