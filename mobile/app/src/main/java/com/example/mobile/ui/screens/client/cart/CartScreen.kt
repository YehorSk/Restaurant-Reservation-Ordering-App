package com.example.mobile.ui.screens.client.cart

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mobile.ui.screens.client.cart.viewmodel.CartScreenViewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.platform.LocalContext
import com.example.mobile.ui.screens.client.cart.components.CartItem

@Composable
fun CartScreen(
    modifier: Modifier = Modifier,
    viewModel: CartScreenViewModel = hiltViewModel()
){
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    if(uiState.error.isNotEmpty()){
        Toast.makeText(context,uiState.error, Toast.LENGTH_LONG).show()
    }

    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        items(uiState.items ?: emptyList()) { item ->
            CartItem(
                menuItem = item,
                onClick = { cartItem ->

                },
            )
        }
    }
}