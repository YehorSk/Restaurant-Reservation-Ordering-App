package com.yehorsk.platea.menu.presentation.menu

import androidx.lifecycle.viewModelScope
import com.yehorsk.platea.cart.domain.repository.CartRepository
import com.yehorsk.platea.core.domain.remote.AppError
import com.yehorsk.platea.core.domain.remote.onError
import com.yehorsk.platea.core.domain.remote.onSuccess
import com.yehorsk.platea.core.utils.ConnectivityObserver
import com.yehorsk.platea.core.utils.snackbar.SnackbarController
import com.yehorsk.platea.core.utils.snackbar.SnackbarEvent
import com.yehorsk.platea.menu.domain.models.Menu
import com.yehorsk.platea.menu.domain.models.MenuItem
import com.yehorsk.platea.menu.domain.repository.MenuRepository
import com.yehorsk.platea.menu.presentation.BaseMenuViewModel
import com.yehorsk.platea.menu.presentation.MenuAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MenuScreenViewModel @Inject constructor(
    menuRepository: MenuRepository,
    cartRepository: CartRepository,
    networkConnectivityObserver: ConnectivityObserver
) : BaseMenuViewModel(menuRepository, cartRepository, networkConnectivityObserver){

    val favoriteUiState: StateFlow<List<MenuItem>> = menuRepository.getFavoriteItems()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = listOf()
        )

    fun onAction(action: MenuAction){
        when(action){
            MenuAction.AddCartItem -> {
                addUserCartItem()
                clearForm()
            }
            MenuAction.AddFavoriteItem -> addUserFavoriteItem()
            MenuAction.ClearForm -> clearForm()
            MenuAction.CloseBottomSheet -> closeBottomSheet()
            MenuAction.DeleteFavoriteItem -> deleteUserFavoriteItem()
            MenuAction.HideMenuDetails -> hideMenuDetails()
            is MenuAction.OnMenuItemClick -> {
                setMenu(action.item)
                updatePrice(action.item.price)
                setMenuItemId(action.item.id)
                setMenuItemFavorite(action.item.isFavorite)
                showBottomSheet()
            }
            is MenuAction.SetMenuFavoriteItem -> setMenuItemFavorite(action.value)
            is MenuAction.ShowMenuDetails -> showMenuDetails(action.menu)
            is MenuAction.UpdatePrice -> updatePrice(action.price)
            is MenuAction.UpdateQuantity -> updateQuantity(action.quantity)
        }
    }

    private fun showMenuDetails(menu: Menu){
        _uiState.update {
            it.copy(
                showMenuDialog = true,
                currentMenu = menu
            )
        }
    }

    private fun hideMenuDetails(){
        _uiState.update {
            it.copy(showMenuDialog = false)
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
        Timber.d("Price $price")
        _uiState.update {
            it.copy(cartForm = it.cartForm.copy(price = price))
        }
    }

    fun updateQuantity(quantity: Int){
        _uiState.update {
            it.copy(cartForm = it.cartForm.copy(quantity = quantity))
        }
    }

    fun setMenuItemId(id: Int){
        _uiState.update {
            it.copy(cartForm = it.cartForm.copy(menuItemId = id))
        }
    }

    fun setMenuItemFavorite(favorite: Boolean){
        _uiState.update {
            it.copy(cartForm = it.cartForm.copy(isFavorite = favorite))
        }
    }

    fun clearForm(){
        _uiState.update {
            it.copy(cartForm = it.cartForm.copy(
                price = 0.00,
                quantity = 1,
                menuItemId = 0,
                isFavorite = false
            ))
        }
    }

    fun addUserCartItem(){
        viewModelScope.launch {
            cartRepository.addUserCartItem(cartForm = _uiState.value.cartForm)
                .onSuccess { data, message ->
                    SnackbarController.sendEvent(
                        event = SnackbarEvent(
                            message = message.toString()
                        )
                    )
                }
                .onError { error ->
                    when(error){
                        is AppError.CONFLICT -> {
                            SnackbarController.sendEvent(
                                event = SnackbarEvent(
                                    message = error.message
                                )
                            )
                        }
                        else -> {
                            SnackbarController.sendEvent(
                                event = SnackbarEvent(
                                    error = error
                                )
                            )
                        }
                    }
                }
            setLoadingState(false)
        }
    }

    fun addUserFavoriteItem(){
        Timber.d("addUserFavoriteItem")
        viewModelScope.launch {
            menuRepository.addFavorite(_uiState.value.currentMenuItem!!.id.toString())
                .onSuccess { data, message ->
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
    }

    fun deleteUserFavoriteItem(){
        Timber.d("deleteUserFavoriteItem")
        viewModelScope.launch {
            menuRepository.deleteFavorite(_uiState.value.currentMenuItem!!.id.toString())
                .onSuccess { data, message ->
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
    }

    fun setMenu(menu: MenuItem){
        _uiState.update {
            it.copy(currentMenuItem = menu)
        }
    }
}
