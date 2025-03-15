package com.yehorsk.platea.cart.presentation.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yehorsk.platea.R
import com.yehorsk.platea.cart.data.db.model.CartItemEntity
import com.yehorsk.platea.cart.data.db.model.toMenuItem
import com.yehorsk.platea.cart.presentation.cart.components.CartItem
import com.yehorsk.platea.cart.presentation.cart.viewmodel.CartScreenViewModel
import com.yehorsk.platea.core.presentation.components.AutoResizedText
import com.yehorsk.platea.core.presentation.components.MenuItemModal
import com.yehorsk.platea.core.presentation.components.NavBar
import com.yehorsk.platea.core.utils.formattedPrice
import com.yehorsk.platea.menu.data.remote.dto.toMenuItemEntity

@Composable
fun CartScreenRoot(
    modifier: Modifier = Modifier,
    viewModel: CartScreenViewModel,
    onGoToCheckoutClick: () -> Unit
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val cartItems by viewModel.cartItemUiState.collectAsStateWithLifecycle()
    val isConnected by viewModel.isNetwork.collectAsStateWithLifecycle(false)

    LaunchedEffect(Unit) {
        viewModel.onAction(CartAction.GetItems)
    }

    CartScreen(
        modifier = modifier,
        uiState = uiState,
        cartItems = cartItems,
        isConnected = isConnected,
        onGoToCheckoutClick = onGoToCheckoutClick,
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    modifier: Modifier = Modifier,
    uiState: CartScreenUiState,
    cartItems: List<CartItemEntity>,
    isConnected: Boolean,
    onGoToCheckoutClick: () -> Unit,
    onAction: (CartAction) -> Unit
){
    PullToRefreshBox(
        isRefreshing = uiState.isLoading,
        onRefresh = { onAction(CartAction.GetItems) }
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            NavBar(
                onGoBack = {  },
                title = R.string.cart,
                showGoBack = false
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ){
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(
                        items = cartItems,
                        key = {it.pivot.id}
                    ) { item ->
                        CartItem(
                            cartItem = item,
                            onClick = { cartItem ->
                                onAction(CartAction.SetItem(cartItem))
                                onAction(CartAction.SetMenuItem(cartItem.toMenuItem().toMenuItemEntity()))
                                onAction(CartAction.ShowBottomSheet)
                            },
                        )
                        HorizontalDivider()
                    }
                    item {
                        if(cartItems.isNotEmpty()){
                            Spacer(modifier = Modifier.height(60.dp))
                        }
                    }
                }
                val checkout = cartItems.sumOf {
                    it.pivot.price
                }
                if(cartItems.isNotEmpty() && isConnected){
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp, start = 16.dp, end = 16.dp)
                            .align(Alignment.BottomCenter),
                        onClick = {
                            onGoToCheckoutClick()
                        }
                    ) {
                        AutoResizedText(
                            modifier = Modifier.padding(8.dp),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            text = stringResource(R.string.go_to_checkout, formattedPrice(checkout)),
                            maxLines = 2
                        )
                    }
                }

            }
        }
    }

    if (uiState.showBottomSheet) {
        uiState.currentItem?.let {
            MenuItemModal(
                menuItem = it,
                onDismiss = {
                    onAction(CartAction.ClearForm)
                },
                cartForm = uiState.cartForm,
                onQuantityChange = { value ->
                    onAction(CartAction.UpdateQuantity(value))
                },
                onPriceChange = {value ->
                    onAction(CartAction.UpdatePrice(value))
                },
                addUserCartItem = {
                    onAction(CartAction.UpdateItem)
                },
                buttonText = R.string.UPDATE,
                addFavoriteItem = {},
                deleteFavoriteItem = {},
                deleteCartItem = {
                    onAction(CartAction.DeleteItem)
                },
                showFavorite = false
            )
        }
    }
}