package com.example.mobile.menu.presentation.menu

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mobile.R
import com.example.mobile.core.EventConsumer
import com.example.mobile.core.domain.SideEffect
import com.example.mobile.menu.presentation.menu.components.MenuHeader
import com.example.mobile.menu.presentation.menu.components.MenuItem
import com.example.mobile.core.presentation.components.MenuItemModal
import com.example.mobile.menu.presentation.menu.viewmodel.MenuScreenViewModel
import com.example.mobile.core.presentation.components.SingleEventEffect
import com.example.mobile.menu.data.db.model.toMenu
import com.example.mobile.menu.data.db.model.toMenuItem
import com.example.mobile.menu.presentation.menu.components.SearchBar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalFoundationApi::class,ExperimentalFoundationApi::class,
    ExperimentalMaterial3Api::class
)
@Composable
fun MenuScreen(
    modifier: Modifier = Modifier,
    viewModel: MenuScreenViewModel,
    onSearchClicked: () -> Unit
){

    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val menuUiState by viewModel.menuUiState.collectAsStateWithLifecycle()
    val isConnected = viewModel.isNetwork.collectAsStateWithLifecycle(false)

    EventConsumer(channel = viewModel.sideEffect) { sideEffect ->
        when(sideEffect){
            is SideEffect.ShowToast -> Toast.makeText(context, sideEffect.message, Toast.LENGTH_SHORT).show()
            SideEffect.NavigateToNextScreen -> {

            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        SearchBar(
            onClick = onSearchClicked,
            enabled = false,
            onValueChange = {},
            isConnected = isConnected.value
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            menuUiState.forEach { menu ->
                stickyHeader {
                    MenuHeader(menuDto = menu.menu.toMenu(menu.menuItems.map { it.toMenuItem() }))
                }
                items(menu.menuItems.map { it }){ item ->
                    MenuItem(
                        menuItem = item,
                        onClick = { menuItem ->
                            viewModel.setMenu(menuItem)
                            viewModel.updatePrice(menuItem.price.toDouble())
                            viewModel.setMenuItemId(menuItem.id)
                            viewModel.setMenuItemFavorite(menuItem.isFavorite)
                            viewModel.showBottomSheet()
                        },
                        isConnected = isConnected.value
                    )
                    HorizontalDivider()
                }
            }
        }
    }

    if (uiState.showBottomSheet) {
        uiState.currentMenu?.let {
            MenuItemModal(
                menuItem = it,
                onDismiss = {
                    viewModel.closeBottomSheet()
                    viewModel.clearForm()
                },
                cartForm = uiState.cartForm,
                onQuantityChange = {viewModel.updateQuantity(it)},
                onPriceChange = {viewModel.updatePrice(it)},
                addUserCartItem = {
                    viewModel.closeBottomSheet()
                    viewModel.addUserCartItem()
                },
                buttonText = R.string.Add,
                addFavoriteItem = {
                    viewModel.addUserFavoriteItem()
                    viewModel.setMenuItemFavorite(true)
                },
                deleteFavoriteItem = {
                    viewModel.deleteUserFavoriteItem()
                    viewModel.setMenuItemFavorite(false)
                }
            )
        }
    }

}

