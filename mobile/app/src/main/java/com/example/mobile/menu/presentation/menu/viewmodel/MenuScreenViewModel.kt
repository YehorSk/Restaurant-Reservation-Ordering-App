package com.example.mobile.menu.presentation.menu.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.mobile.cart.data.remote.CartRepositoryImpl
import com.example.mobile.core.domain.remote.SideEffect
import com.example.mobile.core.domain.remote.onError
import com.example.mobile.core.domain.remote.onSuccess
import com.example.mobile.core.utils.ConnectivityObserver
import com.example.mobile.menu.data.dao.MenuDao
import com.example.mobile.menu.data.db.model.MenuEntity
import com.example.mobile.menu.data.db.model.MenuItemEntity
import com.example.mobile.menu.data.remote.MenuRepositoryImpl
import com.example.mobile.menu.presentation.BaseMenuViewModel
import com.example.mobile.menu.presentation.MenuAction
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
    menuRepositoryImpl: MenuRepositoryImpl,
    cartRepositoryImpl: CartRepositoryImpl,
    networkConnectivityObserver: ConnectivityObserver,
    menuDao: MenuDao
) : BaseMenuViewModel(menuRepositoryImpl, cartRepositoryImpl, networkConnectivityObserver, menuDao){

    val favoriteUiState: StateFlow<List<MenuItemEntity>> = menuDao.getFavoriteItems()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = listOf()
        )

    fun onAction(action: MenuAction){
        when(action){
            MenuAction.AddCartItem -> addUserCartItem()
            MenuAction.AddFavoriteItem -> addUserFavoriteItem()
            MenuAction.ClearForm -> clearForm()
            MenuAction.CloseBottomSheet -> closeBottomSheet()
            MenuAction.DeleteFavoriteItem -> deleteUserFavoriteItem()
            MenuAction.HideMenuDetails -> hideMenuDetails()
            is MenuAction.OnMenuItemClick -> {
                setMenu(action.item)
                updatePrice(action.item.price.toDouble())
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

    fun showMenuDetails(menu: MenuEntity){
        _uiState.update {
            it.copy(
                showMenuDialog = true,
                currentMenu = menu
            )
        }
    }

    fun hideMenuDetails(){
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
            cartRepositoryImpl.addUserCartItem(cartForm = _uiState.value.cartForm)
                .onSuccess { data, message ->
                    _sideEffectChannel.send(SideEffect.ShowSuccessToast(message.toString()))
                }
                .onError { error ->
                    _sideEffectChannel.send(SideEffect.ShowErrorToast(error))
                }
            setLoadingState(false)
        }
    }

    fun addUserFavoriteItem(){
        Timber.d("addUserFavoriteItem")
        viewModelScope.launch {
            menuRepositoryImpl.addFavorite(_uiState.value.currentMenuItem!!.id.toString())
                .onSuccess { data, message ->
                    _sideEffectChannel.send(SideEffect.ShowSuccessToast(message.toString()))
                }
                .onError { error ->
                    _sideEffectChannel.send(SideEffect.ShowErrorToast(error))
                }
        }
    }

    fun deleteUserFavoriteItem(){
        Timber.d("deleteUserFavoriteItem")
        viewModelScope.launch {
            menuRepositoryImpl.deleteFavorite(_uiState.value.currentMenuItem!!.id.toString())
                .onSuccess { data, message ->
                    _sideEffectChannel.send(SideEffect.ShowSuccessToast(message.toString()))
                }
                .onError { error ->
                    _sideEffectChannel.send(SideEffect.ShowErrorToast(error))
                }
        }
    }

    fun setMenu(menu: MenuItemEntity){
        _uiState.update {
            it.copy(currentMenuItem = menu)
        }
    }
}
