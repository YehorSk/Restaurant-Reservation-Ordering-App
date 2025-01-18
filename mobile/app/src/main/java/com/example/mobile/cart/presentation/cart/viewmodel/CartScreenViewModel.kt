package com.example.mobile.cart.presentation.cart.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile.cart.data.dao.CartDao
import com.example.mobile.cart.data.db.model.CartItemEntity
import com.example.mobile.cart.data.remote.CartRepositoryImpl
import com.example.mobile.cart.data.remote.dto.toCartItemEntity
import com.example.mobile.cart.presentation.cart.CartAction
import com.example.mobile.cart.presentation.cart.CartScreenUiState
import com.example.mobile.core.domain.remote.SideEffect
import com.example.mobile.core.domain.remote.onError
import com.example.mobile.core.domain.remote.onSuccess
import com.example.mobile.core.utils.ConnectivityObserver
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
    val networkConnectivityObserver: ConnectivityObserver,
    val cartDao: CartDao
) : ViewModel(){

    private val _uiState = MutableStateFlow(CartScreenUiState())
    val uiState = _uiState.asStateFlow()

    val isNetwork = networkConnectivityObserver
        .observe()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            false
        )

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
        Timber.d("Cart items get init")
        getItems()
    }

    fun onAction(action: CartAction){
        when(action){
            CartAction.CloseBottomSheet -> closeBottomSheet()
            CartAction.DeleteItem -> {
                deleteItem()
                closeBottomSheet()
            }
            CartAction.ShowBottomSheet -> showBottomSheet()
            CartAction.UpdateItem -> {
                updateItem()
                closeBottomSheet()
            }
            CartAction.ClearForm -> {
                clearForm()
                closeBottomSheet()
            }
            is CartAction.UpdatePrice -> updatePrice(action.price)
            is CartAction.UpdateQuantity -> updateQuantity(action.quantity)
            is CartAction.SetItem -> setItem(action.item)
            is CartAction.SetMenuItem -> setMenuItem(action.item)
        }
    }

    fun showBottomSheet(){
        _uiState.update {
            it.copy(showBottomSheet = true)
        }
    }

    fun closeBottomSheet(){
        _uiState.update {
            it.copy(showBottomSheet = false)
        }
    }

    fun updatePrice(price: Double){
        _uiState.update {
            it.copy(cartForm = it.cartForm.copy(price = price))
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

            val localItems = cartItemUiState.value

            cartRepositoryImpl.getAllItems()
                .onSuccess { data, message ->
                    val serverItemIds = data.map { it.id }.toSet()
                    val itemsToDelete = localItems.filter { it.id !in serverItemIds }

                    cartDao.runInTransaction {
                        cartDao.insertItems(data.map { it.toCartItemEntity() })
                        cartDao.deleteItems(itemsToDelete)
                    }

                    _uiState.update {
                        it.copy(
                            error = ""
                        )
                    }
                }
                .onError { error ->
                    _sideEffectChannel.send(SideEffect.ShowErrorToast(error))
                }
            _uiState.update { it.copy(isLoading = false) }
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
            cartRepositoryImpl.updateUserCartItem(_uiState.value.cartForm)
                .onSuccess { data, message ->
                    _sideEffectChannel.send(SideEffect.ShowSuccessToast(message.toString()))
                }
                .onError { error ->
                    _sideEffectChannel.send(SideEffect.ShowErrorToast(error))
                }
        }
        clearForm()
    }

    fun deleteItem(){
        viewModelScope.launch {
            cartRepositoryImpl.deleteUserCartItem(_uiState.value.cartForm)
                .onSuccess { data, message ->
                    _sideEffectChannel.send(SideEffect.ShowSuccessToast(message.toString()))
                }
                .onError { error ->
                    _sideEffectChannel.send(SideEffect.ShowErrorToast(error))
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
