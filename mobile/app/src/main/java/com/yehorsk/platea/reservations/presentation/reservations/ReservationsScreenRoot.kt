package com.yehorsk.platea.reservations.presentation.reservations

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
import com.yehorsk.platea.core.presentation.components.ReservationDropdownList
import com.yehorsk.platea.core.presentation.components.NavBar
import com.yehorsk.platea.core.presentation.components.NavBarWithSearch
import com.yehorsk.platea.reservations.presentation.reservations.components.ReservationsList

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

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val searchText by viewModel.searchText.collectAsStateWithLifecycle()
    val reservations by viewModel.reservationItemUiState.collectAsStateWithLifecycle()
    val filterOption by viewModel.filterOption.collectAsStateWithLifecycle()

    PullToRefreshBox(
        isRefreshing = uiState.isLoading,
        onRefresh = { viewModel.getReservations() }
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
                onTextChanged = { viewModel.onSearchValueChange(it) }
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