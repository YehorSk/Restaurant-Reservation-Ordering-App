package com.example.mobile.menu.presentation.menu

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mobile.R
import com.example.mobile.core.data.repository.SideEffect
import com.example.mobile.menu.presentation.menu.components.MenuHeader
import com.example.mobile.menu.presentation.menu.components.MenuItem
import com.example.mobile.core.presentation.components.MenuItemModal
import com.example.mobile.menu.presentation.menu.viewmodel.MenuScreenViewModel
import com.example.mobile.core.presentation.components.ErrorScreen
import com.example.mobile.core.presentation.components.SingleEventEffect

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MenuScreen(
    modifier: Modifier = Modifier,
    viewModel: MenuScreenViewModel = hiltViewModel()
){

    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val cartForm by viewModel.cartForm.collectAsStateWithLifecycle()
    var showBottomSheet by remember { mutableStateOf(false) }

    SingleEventEffect(viewModel.sideEffectFlow) { sideEffect ->
        when(sideEffect){
            is SideEffect.ShowToast -> Toast.makeText(context, sideEffect.message, Toast.LENGTH_SHORT).show()
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
                },
                buttonText = R.string.Add
            )
        }
    }

}


