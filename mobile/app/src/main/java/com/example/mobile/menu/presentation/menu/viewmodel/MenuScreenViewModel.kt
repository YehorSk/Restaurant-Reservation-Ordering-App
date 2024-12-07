package com.example.mobile.menu.presentation.menu.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.mobile.cart.data.remote.CartRepositoryImpl
import com.example.mobile.core.data.remote.dto.NetworkResult
import com.example.mobile.core.domain.SideEffect
import com.example.mobile.menu.data.remote.MenuRepositoryImpl
import com.example.mobile.menu.data.dao.MenuDao
import com.example.mobile.menu.data.db.model.MenuItemEntity
import com.example.mobile.menu.presentation.BaseMenuViewModel
import com.example.mobile.core.utils.ConnectivityObserver
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
            setLoadingState(true)
            when(val result = cartRepositoryImpl.addUserCartItem(cartForm = _uiState.value.cartForm)){
                is NetworkResult.Error -> {
                    _sideEffectChannel.send(SideEffect.ShowToast(result.message!!))
                }
                is NetworkResult.Success -> {
                    _sideEffectChannel.send(SideEffect.ShowToast(result.message?:""))
                }
            }
            setLoadingState(false)
            clearForm()
        }
    }

    fun addUserFavoriteItem(){
        Timber.d("addUserFavoriteItem")
        viewModelScope.launch {
            setLoadingState(true)
            when(val result = menuRepositoryImpl.addFavorite(_uiState.value.currentMenu!!.id.toString())){
                is NetworkResult.Error -> {
                    _sideEffectChannel.send(SideEffect.ShowToast(result.message!!))
                }
                is NetworkResult.Success -> {
                    _sideEffectChannel.send(SideEffect.ShowToast(result.message?:""))
                }
            }
            setLoadingState(false)
        }
    }

    fun deleteUserFavoriteItem(){
        Timber.d("deleteUserFavoriteItem")
        viewModelScope.launch {
            setLoadingState(true)
            when(val result = menuRepositoryImpl.deleteFavorite(_uiState.value.currentMenu!!.id.toString())){
                is NetworkResult.Error -> {
                    _sideEffectChannel.send(SideEffect.ShowToast(result.message!!))
                }
                is NetworkResult.Success -> {
                    _sideEffectChannel.send(SideEffect.ShowToast(result.message?:""))
                }
            }
            setLoadingState(false)
        }
    }

    fun setMenu(menu: MenuItemEntity){
        _uiState.update {
            it.copy(currentMenu = menu)
        }
    }
}
