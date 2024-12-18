package com.example.mobile.cart.presentation.cart

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mobile.cart.presentation.cart.viewmodel.CartScreenViewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mobile.R
import com.example.mobile.cart.data.db.model.CartItemEntity
import com.example.mobile.cart.data.db.model.toMenuItem
import com.example.mobile.core.domain.SideEffect
import com.example.mobile.cart.presentation.cart.components.CartItem
import com.example.mobile.core.presentation.components.MenuItemModal
import com.example.mobile.core.presentation.components.SingleEventEffect
import com.example.mobile.core.presentation.components.SwipeToDeleteContainer
import com.example.mobile.menu.data.remote.dto.toMenuItemEntity
import com.example.mobile.core.utils.formattedPrice
import com.example.mobile.core.utils.toString

@Composable
fun CartScreenRoot(
    modifier: Modifier = Modifier,
    viewModel: CartScreenViewModel = hiltViewModel(),
    onGoToCheckoutClick: () -> Unit
){
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val cartItems by viewModel.cartItemUiState.collectAsStateWithLifecycle()
    val isConnected by viewModel.isNetwork.collectAsStateWithLifecycle(false)

    SingleEventEffect(viewModel.sideEffectFlow) { sideEffect ->
        when(sideEffect){
            is SideEffect.ShowErrorToast -> Toast.makeText(context, sideEffect.message.toString(context), Toast.LENGTH_SHORT).show()
            is SideEffect.ShowSuccessToast -> Toast.makeText(context, sideEffect.message, Toast.LENGTH_SHORT).show()
            is SideEffect.NavigateToNextScreen -> {}
        }
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

@Composable
fun CartScreen(
    modifier: Modifier = Modifier,
    uiState: CartScreenUiState,
    cartItems: List<CartItemEntity>,
    isConnected: Boolean,
    onGoToCheckoutClick: () -> Unit,
    onAction: (CartAction) -> Unit
){
    Box(
        modifier = modifier
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
                SwipeToDeleteContainer(
                    isInternetError = isConnected,
                    item = item,
                    onDelete = {
                        onAction(CartAction.SetItem(item))
                        onAction(CartAction.DeleteItem)
                    }
                ) {
                    CartItem(
                        cartItem = item,
                        onClick = { cartItem ->
                            onAction(CartAction.SetItem(cartItem))
                            onAction(CartAction.SetMenuItem(cartItem.toMenuItem().toMenuItemEntity()))
                            onAction(CartAction.ShowBottomSheet)
                        },
                    )
                }
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
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = "Go to checkout € ${formattedPrice(checkout)}"
                )
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