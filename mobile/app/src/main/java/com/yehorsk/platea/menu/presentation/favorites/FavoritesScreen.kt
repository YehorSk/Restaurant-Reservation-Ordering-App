package com.yehorsk.platea.menu.presentation.favorites

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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yehorsk.platea.R
import com.yehorsk.platea.core.presentation.components.MenuItemModal
import com.yehorsk.platea.menu.presentation.components.MenuItem
import com.yehorsk.platea.menu.presentation.menu.MenuScreenViewModel
import com.yehorsk.platea.orders.presentation.components.NavBar

@Composable
fun FavoritesScreen(
    modifier: Modifier = Modifier,
    viewModel: MenuScreenViewModel,
    onGoBack: () -> Unit,
    showGoBack: Boolean = false
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val favoriteUiState by viewModel.favoriteUiState.collectAsStateWithLifecycle()


    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        NavBar(
            onGoBack = onGoBack,
            title = R.string.favorites,
            showGoBack = showGoBack
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
                    }
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