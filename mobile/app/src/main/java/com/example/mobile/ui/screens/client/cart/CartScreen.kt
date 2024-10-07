package com.example.mobile.ui.screens.client.cart

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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mobile.ui.screens.client.cart.viewmodel.CartScreenViewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.mobile.R
import com.example.mobile.common.SideEffect
import com.example.mobile.menu.data.model.toMenuItem
import com.example.mobile.ui.screens.client.cart.components.CartItem
import com.example.mobile.ui.screens.client.home.components.MenuItemModal
import com.example.mobile.ui.screens.common.SingleEventEffect

@Composable
fun CartScreen(
    modifier: Modifier = Modifier,
    viewModel: CartScreenViewModel = hiltViewModel()
){
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showBottomSheet by remember { mutableStateOf(false) }
    val cartForm by viewModel.cartForm.collectAsStateWithLifecycle()

    SingleEventEffect(viewModel.sideEffectFlow) { sideEffect ->
        when(sideEffect){
            is SideEffect.ShowToast -> Toast.makeText(context, sideEffect.message, Toast.LENGTH_SHORT).show()
        }
    }

    Box(
        modifier = modifier.fillMaxSize()
    ){
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(uiState.items ?: emptyList()) { item ->
                CartItem(
                    menuItem = item,
                    onClick = { cartItem ->
                        viewModel.setItem(cartItem)
                        viewModel.setMenu(cartItem.toMenuItem())
                        showBottomSheet = true
                    },
                )
            }
            item {
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp, start = 16.dp, end = 16.dp)
                .align(Alignment.BottomCenter),
            onClick = {}
        ) {
            Text(
                modifier = Modifier.padding(8.dp),
                text = "Go to checkout"
            )
        }
    }

    if (showBottomSheet) {
        uiState.currentMenu?.let {
            MenuItemModal(
                menuItem = it,
                onDismiss = {
                    showBottomSheet = false
                    viewModel.clearForm()
                },
                cartForm = cartForm,
                onNoteChange = {viewModel.updateNote(it)},
                onQuantityChange = {viewModel.updateQuantity(it)},
                onPriceChange = {viewModel.updatePrice(it)},
                addUserCartItem = {
                    showBottomSheet = false
                    viewModel.updateItem()
                },
                buttonText = R.string.UPDATE
            )
        }
    }
}