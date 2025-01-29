package com.example.mobile.reservations.presentation.reservations

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
import com.example.mobile.R
import com.example.mobile.core.domain.remote.SideEffect
import com.example.mobile.core.presentation.components.ReservationDropdownList
import com.example.mobile.core.presentation.components.SingleEventEffect
import com.example.mobile.core.utils.toString
import com.example.mobile.orders.presentation.components.NavBar
import com.example.mobile.reservations.presentation.reservations.components.ReservationsList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservationScreenRoot(
    modifier: Modifier = Modifier,
    viewModel: ReservationScreenViewModel = hiltViewModel(),
    onGoToReservationDetails: (Int) -> Unit,
    onGoBack: () -> Unit,
    showGoBack: Boolean,
    @StringRes title: Int = R.string.go_back
){

    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val reservations by viewModel.reservationItemUiState.collectAsStateWithLifecycle()
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
        onRefresh = { viewModel.getReservations() }
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
            ReservationDropdownList(
                filterOption = filterOption,
                text = R.string.filter,
                onSelect = { viewModel.updateFilter(it) }
            )
            ReservationsList(
                items = reservations,
                onGoToReservationDetails = { onGoToReservationDetails(it) }
            )
        }
    }
}