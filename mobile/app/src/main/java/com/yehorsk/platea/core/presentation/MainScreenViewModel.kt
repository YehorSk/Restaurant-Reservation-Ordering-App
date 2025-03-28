package com.yehorsk.platea.core.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yehorsk.platea.cart.data.dao.CartDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import kotlinx.coroutines.flow.stateIn


@HiltViewModel
class MainScreenViewModel @Inject constructor(
    cartDao: CartDao
) : ViewModel() {

    val cartItemCount: StateFlow<Int> = cartDao.getAmountOfItems()
        .stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            0
        )
}