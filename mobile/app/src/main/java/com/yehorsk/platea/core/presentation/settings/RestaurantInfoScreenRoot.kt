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
import com.yehorsk.platea.core.presentation.settings.components.InfoHeader

@Composable
fun RestaurantInfoScreenRoot(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel,
    onGoBack: () -> Unit,
){
    val info by viewModel.restaurantInfoUiState.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    RestaurantInfoScreen(
        modifier = modifier,
        info = info,
        onGoBack = onGoBack,
        isLoading = uiState.isLoading
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantInfoScreen(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    info: RestaurantInfoEntity?,
    onGoBack: () -> Unit,
){
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
        if(!isLoading && info != null){
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ){
                InfoHeader(
                    title = info.name,
                    address = info.address
                )
            }
        }else{
            LoadingPart()
        }
    }
}