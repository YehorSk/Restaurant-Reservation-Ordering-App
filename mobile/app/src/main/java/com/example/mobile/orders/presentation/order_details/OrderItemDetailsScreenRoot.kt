package com.example.mobile.orders.presentation.order_details

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.mobile.R
import com.example.mobile.core.domain.remote.SideEffect
import com.example.mobile.core.presentation.components.SingleEventEffect
import com.example.mobile.core.utils.toString
import com.example.mobile.orders.data.db.model.OrderWithOrderItems
import com.example.mobile.orders.presentation.OrderUiState
import com.example.mobile.orders.presentation.components.NavBar
import kotlin.collections.List

@Composable
fun OrderItemDetailsScreenRoot(
    modifier: Modifier = Modifier,
    viewModel: OrderDetailsViewModel = hiltViewModel(),
    onGoBack: () -> Unit,
    id: Int
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isConnected by viewModel.isNetwork.collectAsStateWithLifecycle(false)
    val ordersUiState by viewModel.ordersUiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    SingleEventEffect(viewModel.sideEffectFlow) { sideEffect ->
        when(sideEffect){
            is SideEffect.NavigateToNextScreen -> onGoBack()
            is SideEffect.ShowErrorToast -> Toast.makeText(context, sideEffect.message.toString(context), Toast.LENGTH_SHORT).show()
            is SideEffect.ShowSuccessToast -> Toast.makeText(context, sideEffect.message, Toast.LENGTH_SHORT).show()
        }
    }
    OrderItemDetailsScreen(
        modifier = modifier,
        ordersUiState = ordersUiState,
        uiState = uiState,
        id = id,
        isConnected = isConnected,
        onGoBack = onGoBack
    )
}

@Composable
fun OrderItemDetailsScreen(
    modifier: Modifier = Modifier ,
    ordersUiState: List<OrderWithOrderItems>,
    uiState: OrderUiState,
    id: Int,
    isConnected: Boolean,
    onGoBack: () -> Unit
){
    val orderItem = ordersUiState.flatMap { it.orderItems }.find { it.pivot.menuItemId == id }
    val contentColor = if(isSystemInDarkTheme()){
        Color.White
    }else{
        Color.Black
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colorScheme.background)
    ) {
        NavBar(
            onGoBack = onGoBack,
            title = R.string.go_back
        )
        if(orderItem != null){
            Column {
                AsyncImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    model = orderItem.picture,
                    contentDescription = "",
                    placeholder = painterResource(R.drawable.menu_item_placeholder),
                    contentScale = ContentScale.Crop,
                    error = painterResource(R.drawable.menu_item_placeholder)
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    text = orderItem.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = contentColor
                )
                Text(
                    modifier = Modifier.padding(top = 8.dp, start = 32.dp, end = 32.dp),
                    fontSize = 16.sp,
                    text = orderItem.recipe,
                    color = contentColor
                )
            }
        }
    }
}