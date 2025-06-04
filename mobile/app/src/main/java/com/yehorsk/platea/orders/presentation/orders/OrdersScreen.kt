package com.yehorsk.platea.orders.presentation.orders

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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yehorsk.platea.R
import com.yehorsk.platea.core.domain.remote.OrderFilter
import com.yehorsk.platea.core.presentation.components.LoadingPart
import com.yehorsk.platea.core.presentation.components.NavBarWithSearch
import com.yehorsk.platea.core.presentation.components.OrdersDropdownList
import com.yehorsk.platea.orders.domain.models.Order
import com.yehorsk.platea.orders.presentation.orders.components.OrdersList

@Composable
fun OrdersScreen(
    modifier:Modifier = Modifier,
    onGoToOrderDetails: (Int) -> Unit,
    viewModel: OrdersViewModel = hiltViewModel(),
    onGoBack: () -> Unit,
    showGoBack: Boolean,
    @StringRes title: Int = R.string.go_back
){

    val orders by viewModel.orderItemsUiState.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val filterOption by viewModel.filterOption.collectAsStateWithLifecycle()
    val searchText by viewModel.searchText.collectAsStateWithLifecycle()
    val userRole by viewModel.userRole.collectAsStateWithLifecycle()

    OrdersScreenRoot(
        modifier = modifier,
        isLoading = uiState.isLoading,
        onGoBack = onGoBack,
        showGoBack = showGoBack,
        title = title,
        onGoToOrderDetails = onGoToOrderDetails,
        searchText = searchText,
        filterOption = filterOption,
        orders = orders,
        onAction = viewModel::onAction,
        showStatus = (userRole in arrayOf("admin", "chef", "waiter"))
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersScreenRoot(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    onGoBack: () -> Unit,
    showGoBack: Boolean,
    @StringRes title: Int = R.string.go_back,
    onGoToOrderDetails: (Int) -> Unit,
    searchText: String,
    filterOption: OrderFilter,
    orders: List<Order>,
    onAction: (OrdersAction) -> Unit,
    showStatus: Boolean = false
){

    PullToRefreshBox(
        isRefreshing = isLoading,
        onRefresh = { onAction(OrdersAction.GetUserOrders) }
    ) {
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            NavBarWithSearch(
                onGoBack = onGoBack,
                showGoBack = showGoBack,
                text = searchText,
                onTextChanged = { onAction(OrdersAction.UpdateSearch(it)) }
            )
            if(isLoading){
                LoadingPart()
            }else{
                OrdersDropdownList(
                    filterOption = filterOption,
                    text = R.string.filter,
                    onSelect = { onAction(OrdersAction.UpdateFilter(it)) }
                )
                OrdersList(
                    orders = orders,
                    onGoToOrderDetails = { onGoToOrderDetails(it) },
                    showStatus = showStatus
                )
            }
        }
    }
}