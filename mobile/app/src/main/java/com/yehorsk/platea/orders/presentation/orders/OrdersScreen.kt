package com.yehorsk.platea.orders.presentation.orders

import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yehorsk.platea.R
import com.yehorsk.platea.core.domain.remote.SideEffect
import com.yehorsk.platea.core.presentation.components.OrdersDropdownList
import com.yehorsk.platea.core.presentation.components.SingleEventEffect
import com.yehorsk.platea.core.utils.toString
import com.yehorsk.platea.orders.presentation.components.NavBar
import com.yehorsk.platea.orders.presentation.orders.components.OrdersList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersScreen(
    modifier:Modifier = Modifier,
    onGoToOrderDetails: (Int) -> Unit,
    viewModel: OrdersViewModel = hiltViewModel(),
    onGoBack: () -> Unit,
    showGoBack: Boolean,
    @StringRes title: Int = R.string.go_back
){

    val context = LocalContext.current
    val orders by viewModel.orderItemsUiState.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val filterOption by viewModel.filterOption.collectAsStateWithLifecycle()

    SingleEventEffect(viewModel.sideEffectFlow) { sideEffect ->
        when(sideEffect){
            is SideEffect.ShowErrorToast -> Toast.makeText(context, sideEffect.message.toString(context), Toast.LENGTH_SHORT).show()
            is SideEffect.ShowSuccessToast -> Toast.makeText(context, sideEffect.message, Toast.LENGTH_SHORT).show()
            is SideEffect.NavigateToNextScreen -> {}
        }
    }

    PullToRefreshBox(
        isRefreshing = uiState.isLoading,
        onRefresh = { viewModel.getUserOrders() }
    ) {
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            NavBar(
                onGoBack = onGoBack,
                title = title,
                showGoBack = showGoBack
            )
            OrdersDropdownList(
                filterOption = filterOption,
                text = R.string.filter,
                onSelect = { viewModel.updateFilter(it) }
            )
            OrdersList(
                orders = orders,
                onGoToOrderDetails = { onGoToOrderDetails(it) }
            )
        }
    }

}