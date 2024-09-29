package com.example.mobile.ui.screens.client.home

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mobile.ui.screens.client.home.components.MenuHeader
import com.example.mobile.ui.screens.client.home.components.MenuItem
import com.example.mobile.ui.screens.client.home.components.MenuItemModal
import com.example.mobile.ui.screens.client.home.viewmodel.ClientMainScreenViewModel
import com.example.mobile.ui.screens.common.ErrorScreen

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: ClientMainScreenViewModel = hiltViewModel()
){

    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val cartForm by viewModel.cartForm.collectAsStateWithLifecycle()
    var showBottomSheet by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.success,uiState.internetError) {
        if(uiState.success.isNotEmpty()){
            Toast.makeText(context,uiState.success,Toast.LENGTH_LONG).show()
        }
        if(uiState.internetError){
            Toast.makeText(context,"No Internet Connection!",Toast.LENGTH_LONG).show()
        }
    }

    if(uiState.internetError){
        ErrorScreen(
            modifier = modifier.fillMaxSize()
        ) {
            viewModel.getMenus()
        }
    }else{
        LazyColumn(
            modifier = modifier.fillMaxSize()
        ) {
            uiState.menus?.forEach { menu ->
                stickyHeader {
                    MenuHeader(menu = menu)
                }
                items(menu.items){ item ->
                    MenuItem(
                        menuItem = item,
                        onClick = { menuItem ->
                            viewModel.setMenu(menuItem)
                            viewModel.updatePrice(menuItem.price.toDouble())
                            viewModel.setMenuItemId(menuItem.id)
                            showBottomSheet = true
                        }
                    )
                }
            }
        }
    }

    if (showBottomSheet) {
        uiState.currentMenu?.let {
            MenuItemModal(
                menuItem = it,
                onDismiss = {
                                showBottomSheet = false
                                viewModel.clearForm()
                            },
                cartForm = cartForm,
                onNoteChange = {viewModel.updateNote(it)},
                onQuantityChange = {viewModel.updateQuantity(it)},
                onPriceChange = {viewModel.updatePrice(it)},
                addUserCartItem = {
                    showBottomSheet = false
                    viewModel.addUserCartItem()
                }
            )
        }
    }

}


