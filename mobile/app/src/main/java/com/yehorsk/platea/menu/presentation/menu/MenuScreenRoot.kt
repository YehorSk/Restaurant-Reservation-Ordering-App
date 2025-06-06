package com.yehorsk.platea.menu.presentation.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmadhamwi.tabsync_compose.lazyListTabSync
import com.yehorsk.platea.R
import com.yehorsk.platea.core.presentation.components.LoadingPart
import com.yehorsk.platea.core.presentation.components.MenuItemModal
import com.yehorsk.platea.menu.domain.models.Menu
import com.yehorsk.platea.menu.presentation.MenuAction
import com.yehorsk.platea.menu.presentation.components.MenuDetailsDialog
import com.yehorsk.platea.menu.presentation.components.MenuList
import com.yehorsk.platea.menu.presentation.components.MenuTabBar
import com.yehorsk.platea.menu.presentation.components.SearchBar

@Composable
fun MenuScreenRoot(
    modifier: Modifier = Modifier,
    viewModel: MenuScreenViewModel,
    onSearchClicked: () -> Unit,
    onCreateReservationClicked: () -> Unit,
    isUser: Boolean
){

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val menuUiState by viewModel.menuUiState.collectAsStateWithLifecycle()
    val isConnected by viewModel.isNetwork.collectAsStateWithLifecycle()

    if(uiState.isLoading){
        LoadingPart()
    }else {
        MenuScreen(
            modifier = modifier,
            uiState = uiState,
            menuUiState = menuUiState,
            isConnected = isConnected,
            onSearchClicked = { onSearchClicked() },
            onCreateReservationClicked = { onCreateReservationClicked() },
            onAction = viewModel::onAction,
            isUser = isUser
        )
    }

}

@Composable
fun MenuScreen(
    modifier: Modifier = Modifier,
    uiState: MenuScreenUiState,
    menuUiState: List<Menu>,
    isConnected: Boolean,
    onSearchClicked: () -> Unit,
    onCreateReservationClicked: () -> Unit,
    onAction: (MenuAction) -> Unit,
    isUser: Boolean
){

    if(menuUiState.isNotEmpty()){

        val (selectedTabIndex, setSelectedTabIndex, lazyListState) = lazyListTabSync(menuUiState.map { it.id }.indices.toList())

        Box(
            modifier = modifier
                .fillMaxSize()
        ){
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                SearchBar(
                    onClick = onSearchClicked,
                    enabled = false,
                    onValueChange = {},
                    isConnected = isConnected
                )
                MenuTabBar(
                    menus = menuUiState,
                    selectedMenu = selectedTabIndex,
                    onMenuClicked = {index, _ -> setSelectedTabIndex(index) }
                )
                MenuList(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background),
                    items = menuUiState,
                    onClick = {
                            menuItem -> onAction(MenuAction.OnMenuItemClick(menuItem))
                    },
                    listState = lazyListState
                )
            }
            if(isUser){
                ExtendedFloatingActionButton(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp),
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    onClick = { onCreateReservationClicked() },
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Event,
                            contentDescription = null,
                            tint = Color.White
                        )
                    },
                    text = {
                            Text(
                                text = stringResource(R.string.book),
                                color = Color.White,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                            )
                           },
                )
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
                    onAction(MenuAction.ClearForm)
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

