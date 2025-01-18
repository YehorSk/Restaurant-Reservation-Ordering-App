package com.example.mobile.menu.presentation.menu

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmadhamwi.tabsync_compose.lazyListTabSync
import com.example.mobile.R
import com.example.mobile.core.domain.remote.SideEffect
import com.example.mobile.core.presentation.components.LoadingPart
import com.example.mobile.core.presentation.components.MenuItemModal
import com.example.mobile.core.utils.EventConsumer
import com.example.mobile.core.utils.toString
import com.example.mobile.menu.data.db.model.MenuWithMenuItems
import com.example.mobile.menu.presentation.MenuAction
import com.example.mobile.menu.presentation.components.MenuDetailsDialog
import com.example.mobile.menu.presentation.components.MenuList
import com.example.mobile.menu.presentation.components.MenuTabBar
import com.example.mobile.menu.presentation.components.SearchBar
import com.example.mobile.menu.presentation.menu.viewmodel.MenuScreenViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalFoundationApi::class,ExperimentalFoundationApi::class,
    ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class
)
@Composable
fun MenuScreenRoot(
    modifier: Modifier = Modifier,
    viewModel: MenuScreenViewModel,
    onSearchClicked: () -> Unit,
    onCreateReservationClicked: () -> Unit,
    isUser: Boolean
){

    val locationPermissionsState = rememberMultiplePermissionsState(
        listOf(
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
        )
    )

    LaunchedEffect(locationPermissionsState) {
        if(!locationPermissionsState.allPermissionsGranted){
            locationPermissionsState.launchMultiplePermissionRequest()
        }
    }

    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val menuUiState by viewModel.menuUiState.collectAsStateWithLifecycle()
    val isConnected by viewModel.isNetwork.collectAsStateWithLifecycle()

    EventConsumer(channel = viewModel.sideEffect) { sideEffect ->
        when(sideEffect){
            is SideEffect.ShowErrorToast -> Toast.makeText(context, sideEffect.message.toString(context), Toast.LENGTH_SHORT).show()
            is SideEffect.ShowSuccessToast -> Toast.makeText(context, sideEffect.message, Toast.LENGTH_SHORT).show()
            is SideEffect.NavigateToNextScreen -> {}
        }
    }
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MenuScreen(
    modifier: Modifier = Modifier,
    uiState: MenuScreenUiState,
    menuUiState: List<MenuWithMenuItems>,
    isConnected: Boolean,
    onSearchClicked: () -> Unit,
    onCreateReservationClicked: () -> Unit,
    onAction: (MenuAction) -> Unit,
    isUser: Boolean
){

    if(menuUiState.isNotEmpty()){

        val (selectedTabIndex, setSelectedTabIndex, lazyListState) = lazyListTabSync(menuUiState.map { it.menu.id }.indices.toList())

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
                    menus = menuUiState.map { it.menu },
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
                    containerColor = MaterialTheme.colorScheme.primary,
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
                                color = Color.White
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

