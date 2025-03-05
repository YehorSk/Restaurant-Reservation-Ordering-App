package com.yehorsk.platea.cart.presentation.cart.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yehorsk.platea.cart.data.dao.CartDao
import com.yehorsk.platea.cart.data.db.model.CartItemEntity
import com.yehorsk.platea.cart.data.remote.CartRepositoryImpl
import com.yehorsk.platea.cart.data.remote.dto.toCartItemEntity
import com.yehorsk.platea.cart.presentation.cart.CartAction
import com.yehorsk.platea.cart.presentation.cart.CartScreenUiState
import com.yehorsk.platea.core.utils.SideEffect
import com.yehorsk.platea.core.domain.remote.onError
import com.yehorsk.platea.core.domain.remote.onSuccess
import com.yehorsk.platea.core.utils.ConnectivityObserver
import com.yehorsk.platea.core.utils.snackbar.SnackbarController
import com.yehorsk.platea.core.utils.snackbar.SnackbarEvent
import com.yehorsk.platea.menu.data.db.model.MenuItemEntity
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

    val isNetwork = MutableStateFlow<Boolean>(networkConnectivityObserver.isAvailable)

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
        viewModelScope.launch{
            networkConnectivityObserver.observe().collect { status ->
                isNetwork.value = status
            }
        }
        Timber.d("Cart items get init")
        getItems()
    }

    fun onAction(action: CartAction){
        when(action){
            CartAction.GetItems -> getItems()
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

    private fun closeBottomSheet(){
        _uiState.update {
            it.copy(showBottomSheet = false)
        }
    }

    private fun updatePrice(price: Double){
        _uiState.update {
            it.copy(cartForm = it.cartForm.copy(price = price))
        }
    }

    private fun updateQuantity(quantity: Int){
        _uiState.update {
            it.copy(cartForm = it.cartForm.copy(quantity = quantity))
        }
    }

    private fun clearForm(){
        _uiState.update {
            it.copy(cartForm = it.cartForm.copy(
                price = 0.00,
                quantity = 1,
                menuItemId = 0
            ))
        }
    }

    private fun getItems() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            cartRepositoryImpl.getAllItems()
                .onError { error ->
                    SnackbarController.sendEvent(
                        event = SnackbarEvent(
                            error = error
                        )
                    )
                }
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    private fun setItem(item: CartItemEntity){
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

    private fun updateItem(){
        viewModelScope.launch {
            cartRepositoryImpl.updateUserCartItem(_uiState.value.cartForm)
                .onSuccess { _, message ->
                    SnackbarController.sendEvent(
                        event = SnackbarEvent(
                            message = message.toString()
                        )
                    )
                }
                .onError { error ->
                    SnackbarController.sendEvent(
                        event = SnackbarEvent(
                            error = error
                        )
                    )
                }
        }
        clearForm()
    }

    private fun deleteItem(){
        viewModelScope.launch {
            cartRepositoryImpl.deleteUserCartItem(_uiState.value.cartForm)
                .onSuccess { _, message ->
                    SnackbarController.sendEvent(
                        event = SnackbarEvent(
                            message = message.toString()
                        )
                    )
                }
                .onError { error ->
                    SnackbarController.sendEvent(
                        event = SnackbarEvent(
                            error = error
                        )
                    )
                }
        }
        clearForm()
    }

    private fun setMenuItem(menu: MenuItemEntity){
        _uiState.update {
            it.copy(currentItem = menu)
        }
    }

}
