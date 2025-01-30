package com.yehorsk.platea.cart.presentation.cart

import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yehorsk.platea.R
import com.yehorsk.platea.cart.data.db.model.CartItemEntity
import com.yehorsk.platea.cart.data.db.model.toMenuItem
import com.yehorsk.platea.cart.presentation.cart.components.CartItem
import com.yehorsk.platea.cart.presentation.cart.viewmodel.CartScreenViewModel
import com.yehorsk.platea.core.domain.remote.SideEffect
import com.yehorsk.platea.core.presentation.components.MenuItemModal
import com.yehorsk.platea.core.presentation.components.SingleEventEffect
import com.yehorsk.platea.core.utils.formattedPrice
import com.yehorsk.platea.core.utils.toString
import com.yehorsk.platea.menu.data.remote.dto.toMenuItemEntity
import com.yehorsk.platea.orders.presentation.components.NavBar

@Composable
fun CartScreenRoot(
    modifier: Modifier = Modifier,
    viewModel: CartScreenViewModel,
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
            if(cartItems.isNotEmpty()){
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
                if(isConnected){
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
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            text = stringResource(R.string.go_to_checkout, formattedPrice(checkout))
                        )
                    }
                }
            }else{
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painterResource(R.drawable.empty_cart),
                        contentDescription = ""
                    )
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