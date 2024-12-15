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
import com.example.mobile.menu.data.db.model.MenuWithMenuItems
import com.example.mobile.menu.presentation.MenuAction
import com.example.mobile.menu.presentation.menu.viewmodel.MenuScreenViewModel
import com.example.mobile.menu.presentation.menu.components.MenuDetailsDialog
import com.example.mobile.menu.presentation.menu.components.SearchBar

@OptIn(ExperimentalFoundationApi::class,ExperimentalFoundationApi::class,
    ExperimentalMaterial3Api::class
)
@Composable
fun MenuScreenRoot(
    modifier: Modifier = Modifier,
    viewModel: MenuScreenViewModel,
    onSearchClicked: () -> Unit
){

    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val menuUiState by viewModel.menuUiState.collectAsStateWithLifecycle()
    val isConnected by viewModel.isNetwork.collectAsStateWithLifecycle(false)

    EventConsumer(channel = viewModel.sideEffect) { sideEffect ->
        when(sideEffect){
            is SideEffect.ShowToast -> Toast.makeText(context, sideEffect.message, Toast.LENGTH_SHORT).show()
            SideEffect.NavigateToNextScreen -> {

            }
        }
    }
    
    MenuScreen(
        modifier = modifier,
        uiState = uiState,
        menuUiState = menuUiState,
        isConnected = isConnected,
        onSearchClicked = { onSearchClicked() },
        onAction = viewModel::onAction
    )

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MenuScreen(
    modifier: Modifier = Modifier,
    uiState: MenuScreenUiState,
    menuUiState: List<MenuWithMenuItems>,
    isConnected: Boolean,
    onSearchClicked: () -> Unit,
    onAction: (MenuAction) -> Unit
){

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        SearchBar(
            onClick = onSearchClicked,
            enabled = false,
            onValueChange = {},
            isConnected = isConnected
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            menuUiState.forEach { menu ->
                stickyHeader {
                    MenuHeader(
                        menuDto = menu.menu,
                        onMenuClick = { onAction(MenuAction.ShowMenuDetails(menu.menu)) }
                    )
                }
                items(menu.menuItems.map { it }){ item ->
                    MenuItem(
                        menuItem = item,
                        onClick = { menuItem ->
                            onAction(MenuAction.OnMenuItemClick(menuItem))
                        },
                        isConnected = isConnected
                    )
                    HorizontalDivider()
                }
            }
        }
    }

    if (uiState.showBottomSheet) {
        uiState.currentMenuItem?.let {
            MenuItemModal(
                menuItem = it,
                onDismiss = {
                    onAction(MenuAction.CloseBottomSheet)
                    onAction(MenuAction.ClearForm)
                },
                cartForm = uiState.cartForm,
                onQuantityChange = {
                    onAction(MenuAction.UpdateQuantity(it))
                                   },
                onPriceChange = {
                    onAction(MenuAction.UpdatePrice(it))
                                },
                addUserCartItem = {
                    onAction(MenuAction.CloseBottomSheet)
                    onAction(MenuAction.AddCartItem)
                },
                buttonText = R.string.Add,
                addFavoriteItem = {
                    onAction(MenuAction.AddFavoriteItem)
                    onAction(MenuAction.SetMenuFavoriteItem(true))
                },
                deleteFavoriteItem = {
                    onAction(MenuAction.DeleteFavoriteItem)
                    onAction(MenuAction.SetMenuFavoriteItem(false))
                }
            )
        }
    }

    if(uiState.showMenuDialog){
        MenuDetailsDialog(
            menu = uiState.currentMenu!!,
            onDismissRequest = {
                onAction(MenuAction.HideMenuDetails)
            }
        )
    }
}

