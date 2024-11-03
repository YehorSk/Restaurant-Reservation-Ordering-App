package com.example.mobile.menu.presentation.menu.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile.cart.data.remote.CartRepositoryImpl
import com.example.mobile.core.data.remote.dto.NetworkResult
import com.example.mobile.core.data.repository.SideEffect
import com.example.mobile.menu.data.remote.MenuRepositoryImpl
import com.example.mobile.core.presentation.components.CartForm
import com.example.mobile.menu.data.dao.MenuDao
import com.example.mobile.menu.data.db.model.MenuItemEntity
import com.example.mobile.menu.data.db.model.MenuWithMenuItems
import com.example.mobile.menu.presentation.menu.MenuScreenUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MenuScreenViewModel @Inject constructor(
    val menuRepositoryImpl: MenuRepositoryImpl,
    val cartRepositoryImpl: CartRepositoryImpl,
    val menuDao: MenuDao
) : ViewModel(){

    private val _uiState = MutableStateFlow(MenuScreenUiState())
    val uiState: StateFlow<MenuScreenUiState> = _uiState.asStateFlow()

    val menuUiState: StateFlow<List<MenuWithMenuItems>> = menuDao.getMenuWithMenuItems()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = listOf()
        )

    val favoriteUiState: StateFlow<List<MenuItemEntity>> = menuDao.getFavoriteItems()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = listOf()
        )

    private val _sideEffectChannel = Channel<SideEffect>(capacity = Channel.BUFFERED)
    val sideEffectFlow: Flow<SideEffect>
        get() = _sideEffectChannel.receiveAsFlow()

    init {
        getMenus()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val searchUiState: StateFlow<List<MenuItemEntity>> = _uiState
        .map { it.searchText }
        .distinctUntilChanged()
        .flatMapLatest { query ->
            menuDao.searchItems("%$query%")
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = listOf()
        )

    private fun setLoadingState(isLoading: Boolean) {
        _uiState.update { it.copy(isLoading = isLoading) }
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

    fun onSearchValueChange(value: String){
        _uiState.update {
            it.copy(searchText = value)
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
            _uiState.update { state ->
                when(val result = cartRepositoryImpl.addUserCartItem(cartForm = _uiState.value.cartForm)){
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
                        _sideEffectChannel.send(SideEffect.ShowToast(result.message?:""))
                        state.copy()
                    }
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
                    _sideEffectChannel.send(SideEffect.ShowToast("No internet connection!"))
                    _uiState.update { state ->
                        state.copy(
                            internetError = true
                        )
                    }
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
                    _sideEffectChannel.send(SideEffect.ShowToast("No internet connection!"))
                    _uiState.update { state ->
                        state.copy(
                            internetError = true
                        )
                    }
                }
                is NetworkResult.Success -> {
                    _sideEffectChannel.send(SideEffect.ShowToast(result.message?:""))
                }
            }
            setLoadingState(false)
        }
    }

    fun getMenus(){
        viewModelScope.launch {
            setLoadingState(true)
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
                    is NetworkResult.Success ->{
                        state.copy(
                            internetError = false
                        )
                    }

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
