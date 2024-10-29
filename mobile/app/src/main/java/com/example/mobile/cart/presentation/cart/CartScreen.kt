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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.mobile.R
import com.example.mobile.cart.data.db.model.toMenuItem
import com.example.mobile.core.data.repository.SideEffect
import com.example.mobile.cart.presentation.cart.components.CartItem
import com.example.mobile.core.presentation.components.MenuItemModal
import com.example.mobile.core.presentation.components.SingleEventEffect
import com.example.mobile.core.presentation.components.SwipeToDeleteContainer
import com.example.mobile.menu.data.remote.dto.toMenuItemEntity

@Composable
fun CartScreen(
    modifier: Modifier = Modifier,
    viewModel: CartScreenViewModel
){
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val cartItems by viewModel.cartItemUiState.collectAsStateWithLifecycle()
    var showBottomSheet by remember { mutableStateOf(false) }

    SingleEventEffect(viewModel.sideEffectFlow) { sideEffect ->
        when(sideEffect){
            is SideEffect.ShowToast -> {
                Toast.makeText(context, sideEffect.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

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
                    isInternetError = uiState.internetError,
                    item = item,
                    onDelete = {
                        viewModel.setItem(item)
                        viewModel.deleteItem()
                    }
                ) {
                    CartItem(
                        cartItem = item,
                        onClick = { cartItem ->
                            viewModel.setItem(cartItem)
                            viewModel.setMenuItem(cartItem.toMenuItem().toMenuItemEntity())
                            showBottomSheet = true
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
        if(cartItems.isNotEmpty()){
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp, start = 16.dp, end = 16.dp)
                    .align(Alignment.BottomCenter),
                onClick = {}
            ) {
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = "Go to checkout ${String.format("%.2f", checkout)} €"
                )
            }
        }
    }

    if (showBottomSheet) {
        uiState.currentItem?.let {
            MenuItemModal(
                menuItem = it,
                onDismiss = {
                    showBottomSheet = false
                    viewModel.clearForm()
                },
                cartForm = uiState.cartForm,
                onQuantityChange = {value -> viewModel.updateQuantity(value)},
                onPriceChange = {value -> viewModel.updatePrice(value)},
                addUserCartItem = {
                    showBottomSheet = false
                    viewModel.updateItem()
                },
                buttonText = R.string.UPDATE
            )
        }
    }
}