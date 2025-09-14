package com.yehorsk.platea.cart.presentation.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yehorsk.platea.cart.domain.models.CartItem
import com.yehorsk.platea.cart.domain.repository.CartRepository
import com.yehorsk.platea.core.domain.remote.onError
import com.yehorsk.platea.core.domain.remote.onSuccess
import com.yehorsk.platea.core.utils.ConnectivityObserver
import com.yehorsk.platea.core.utils.snackbar.SnackbarController
import com.yehorsk.platea.core.utils.snackbar.SnackbarEvent
import com.yehorsk.platea.menu.domain.models.MenuItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartScreenViewModel @Inject constructor(
    val cartRepository: CartRepository,
    val networkConnectivityObserver: ConnectivityObserver
) : ViewModel(){

    private val _uiState = MutableStateFlow(CartScreenUiState())
    val uiState = _uiState.asStateFlow()

    init {
        observeCartItems()
        observeNetwork()
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

    private fun observeNetwork(){
        networkConnectivityObserver.observe()
            .onEach { network ->
                _uiState.update { it.copy(
                    isNetwork = network
                ) }
            }
            .launchIn(viewModelScope)
    }


    private fun observeCartItems(){
        cartRepository.getAllItemsFlow()
            .onEach { items ->
                _uiState.update { it.copy(
                    items = items
                ) }
            }
            .launchIn(viewModelScope)
    }

    private fun showBottomSheet(){
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
            cartRepository.getAllItems()
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

    private fun setItem(item: CartItem){
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
            cartRepository.updateUserCartItem(_uiState.value.cartForm)
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
            cartRepository.deleteUserCartItem(_uiState.value.cartForm)
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

    private fun setMenuItem(menu: MenuItem){
        _uiState.update {
            it.copy(currentItem = menu)
        }
    }

}