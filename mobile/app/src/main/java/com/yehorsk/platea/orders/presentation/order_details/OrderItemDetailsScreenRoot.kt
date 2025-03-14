package com.yehorsk.platea.orders.presentation.order_details

import androidx.compose.foundation.background
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.yehorsk.platea.R
import com.yehorsk.platea.core.presentation.components.NavBar
import com.yehorsk.platea.core.presentation.components.SingleEventEffect
import com.yehorsk.platea.core.utils.SideEffect
import com.yehorsk.platea.orders.data.db.model.OrderWithOrderItems
import kotlin.collections.List

@Composable
fun OrderItemDetailsScreenRoot(
    modifier: Modifier = Modifier,
    viewModel: OrderDetailsViewModel = hiltViewModel(),
    onGoBack: () -> Unit,
    id: Int
){

    val ordersUiState by viewModel.ordersUiState.collectAsStateWithLifecycle()

    SingleEventEffect(viewModel.sideEffectFlow) { sideEffect ->
        when(sideEffect){
            is SideEffect.NavigateToNextScreen -> onGoBack()
            is SideEffect.ShowErrorToast -> {}
            is SideEffect.ShowSuccessToast -> {}
        }
    }
    OrderItemDetailsScreen(
        modifier = modifier,
        ordersUiState = ordersUiState,
        id = id,
        onGoBack = onGoBack
    )
}

@Composable
fun OrderItemDetailsScreen(
    modifier: Modifier = Modifier ,
    ordersUiState: List<OrderWithOrderItems>,
    id: Int,
    onGoBack: () -> Unit
){
    val orderItem = ordersUiState.flatMap { it.orderItems }.find { it.pivot.menuItemId == id }
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
                )
                Text(
                    modifier = Modifier.padding(top = 8.dp, start = 32.dp, end = 32.dp),
                    fontSize = 16.sp,
                    text = orderItem.recipe,
                )
            }
        }
    }
}