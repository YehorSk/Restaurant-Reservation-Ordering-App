package com.yehorsk.platea.core.presentation.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import com.yehorsk.platea.core.data.db.models.RestaurantInfoEntity
import com.yehorsk.platea.core.presentation.components.LoadingPart
import com.yehorsk.platea.core.presentation.components.NavBar

@Composable
fun RestaurantInfoScreenRoot(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel,
    onGoBack: () -> Unit,
){
    val info by viewModel.restaurantInfoUiState.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(true) {
        viewModel::getRestaurantInfo
    }

    RestaurantInfoScreen(
        modifier = modifier,
        info = info,
        onGoBack = onGoBack,
        onRefresh = viewModel::getRestaurantInfo,
        isLoading = uiState.isLoading
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantInfoScreen(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    onRefresh: () -> Unit,
    info: RestaurantInfoEntity?,
    onGoBack: () -> Unit,
){
    PullToRefreshBox(
        isRefreshing = isLoading,
        onRefresh = { onRefresh() }
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            NavBar(
                onGoBack = { onGoBack() }
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ){
                if(!isLoading){
                    Text(
                        text = "${info?:""}"
                    )
                }else{
                    LoadingPart()
                }
            }
        }
    }
}