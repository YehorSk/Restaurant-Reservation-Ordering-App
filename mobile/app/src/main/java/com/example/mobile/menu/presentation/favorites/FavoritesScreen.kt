package com.example.mobile.menu.presentation.favorites

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mobile.R
import com.example.mobile.core.utils.EventConsumer
import com.example.mobile.core.domain.remote.SideEffect
import com.example.mobile.core.presentation.components.MenuItemModal
import com.example.mobile.core.utils.toString
import com.example.mobile.menu.presentation.components.MenuItem
import com.example.mobile.menu.presentation.menu.viewmodel.MenuScreenViewModel
import com.example.mobile.orders.presentation.components.NavBar

@Composable
fun FavoritesScreen(
    modifier: Modifier = Modifier,
    viewModel: MenuScreenViewModel,
    onGoBack: () -> Unit
){
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val favoriteUiState by viewModel.favoriteUiState.collectAsStateWithLifecycle()

    EventConsumer(channel = viewModel.sideEffect) { sideEffect ->
        when(sideEffect){
            is SideEffect.ShowErrorToast -> Toast.makeText(context, sideEffect.message.toString(context), Toast.LENGTH_SHORT).show()
            is SideEffect.ShowSuccessToast -> Toast.makeText(context, sideEffect.message, Toast.LENGTH_SHORT).show()
            is SideEffect.NavigateToNextScreen -> {}
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        NavBar(
            onGoBack = onGoBack,
            title = R.string.go_back
        )
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(
                items = favoriteUiState,
                key = { it.id }
            ){ item ->
                MenuItem(
                    menuItem = item,
                    onClick = { menuItem ->
                        viewModel.setMenu(menuItem)
                        viewModel.updatePrice(menuItem.price.toDouble())
                        viewModel.setMenuItemId(menuItem.id)
                        viewModel.setMenuItemFavorite(menuItem.isFavorite)
                        viewModel.showBottomSheet()
                    },
                    isConnected = true
                )
                HorizontalDivider()
            }

        }
    }
    if (uiState.showBottomSheet) {
        uiState.currentMenuItem?.let {
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