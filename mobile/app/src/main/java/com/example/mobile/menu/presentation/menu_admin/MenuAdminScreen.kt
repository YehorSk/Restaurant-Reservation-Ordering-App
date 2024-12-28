package com.example.mobile.menu.presentation.menu_admin

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mobile.core.utils.EventConsumer
import com.example.mobile.core.domain.remote.SideEffect
import com.example.mobile.menu.presentation.components.MenuHeader
import com.example.mobile.menu.presentation.components.MenuItem
import com.example.mobile.menu.presentation.components.MenuList
import com.example.mobile.menu.presentation.components.SearchBar
import com.example.mobile.menu.presentation.menu_admin.viewmodel.MenuAdminScreenViewModel

@OptIn(ExperimentalFoundationApi::class,ExperimentalFoundationApi::class,
    ExperimentalMaterial3Api::class
)
@Composable
fun MenuAdminScreen(
    modifier: Modifier = Modifier,
    viewModel: MenuAdminScreenViewModel,
    onSearchClicked: () -> Unit
){
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val menuUiState by viewModel.menuUiState.collectAsStateWithLifecycle()
    val isConnected by viewModel.isNetwork.collectAsStateWithLifecycle()

    EventConsumer(channel = viewModel.sideEffect) { sideEffect ->
        when(sideEffect){
            is SideEffect.ShowErrorToast -> Toast.makeText(context, sideEffect.message.toString(), Toast.LENGTH_SHORT).show()
            is SideEffect.ShowSuccessToast -> Toast.makeText(context, sideEffect.message, Toast.LENGTH_SHORT).show()
            is SideEffect.NavigateToNextScreen -> {}
        }
    }
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        SearchBar(
            onClick = onSearchClicked,
            enabled = false,
            onValueChange = {},
            isConnected = true
        )
        MenuList(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            items = menuUiState,
            onClick = {},
            isConnected = isConnected
        )
    }
}