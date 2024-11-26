package com.example.mobile.menu.presentation.menu_admin.viewmodel

import com.example.mobile.cart.data.remote.CartRepositoryImpl
import com.example.mobile.menu.data.dao.MenuDao
import com.example.mobile.menu.data.remote.MenuRepositoryImpl
import com.example.mobile.menu.presentation.BaseMenuViewModel
import com.example.mobile.core.utils.ConnectivityObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MenuAdminScreenViewModel @Inject constructor(
    menuRepositoryImpl: MenuRepositoryImpl,
    cartRepositoryImpl: CartRepositoryImpl,
    networkConnectivityObserver: ConnectivityObserver,
    menuDao: MenuDao
) : BaseMenuViewModel(menuRepositoryImpl, cartRepositoryImpl, networkConnectivityObserver, menuDao){


}