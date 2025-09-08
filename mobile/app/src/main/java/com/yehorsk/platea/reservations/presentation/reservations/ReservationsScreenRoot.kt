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
import com.yehorsk.platea.core.domain.remote.ReservationFilter
import com.yehorsk.platea.core.presentation.components.LoadingPart
import com.yehorsk.platea.core.presentation.components.NavBarWithSearch
import com.yehorsk.platea.core.presentation.components.ReservationDropdownList
import com.yehorsk.platea.reservations.presentation.reservations.components.ReservationsList

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
    val filterOption by viewModel.filterOption.collectAsStateWithLifecycle()

    ReservationScreen(
        modifier = modifier,
        uiState = uiState,
        onGoToReservationDetails = { onGoToReservationDetails(it) },
        onGoBack = { onGoBack() },
        searchText = searchText,
        filterOption = filterOption,
        showGoBack = showGoBack,
        onAction = viewModel::onAction,
        showStatus = (uiState.userRole in arrayOf("admin", "chef", "waiter")),
        isLoading = uiState.isLoading
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservationScreen(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    uiState: ReservationUiState,
    onGoToReservationDetails: (Int) -> Unit,
    onGoBack: () -> Unit,
    searchText: String,
    filterOption: ReservationFilter,
    showGoBack: Boolean,
    onAction: (ReservationScreenAction) -> Unit,
    showStatus: Boolean = false
){
    PullToRefreshBox(
        isRefreshing = uiState.isLoading,
        onRefresh = { onAction(ReservationScreenAction.GetReservations) }
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
                onTextChanged = { onAction(ReservationScreenAction.OnSearchValueChange(it)) }
            )
            if(isLoading){
                LoadingPart()
            }else{
                ReservationDropdownList(
                    filterOption = filterOption,
                    text = R.string.filter,
                    onSelect = { onAction(ReservationScreenAction.UpdateFilter(it)) }
                )
                ReservationsList(
                    items = uiState.sectionedReservations,
                    onGoToReservationDetails = { onGoToReservationDetails(it) },
                    showStatus = showStatus
                )
            }
        }
    }
}