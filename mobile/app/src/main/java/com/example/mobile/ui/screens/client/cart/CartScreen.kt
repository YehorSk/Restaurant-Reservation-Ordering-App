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
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
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
}