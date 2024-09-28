package com.example.mobile.ui.screens.client.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mobile.ui.screens.client.home.components.MenuHeader
import com.example.mobile.ui.screens.client.home.components.MenuItem
import com.example.mobile.ui.screens.client.home.components.MenuItemModal
import com.example.mobile.ui.screens.client.home.viewmodel.ClientMainScreenViewModel
import com.example.mobile.ui.screens.common.ErrorScreen
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: ClientMainScreenViewModel = hiltViewModel()
){

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showBottomSheet by remember { mutableStateOf(false) }

    var isRefreshing by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val pullToRefreshState = rememberPullToRefreshState()


    if(uiState.internetError){
        ErrorScreen(
            modifier = modifier.fillMaxSize()
        ) {
            viewModel.getMenus()
        }
    }else{
        PullToRefreshBox(
            state = pullToRefreshState,
            isRefreshing = isRefreshing,
            onRefresh = {viewModel.getMenus()},
            modifier = modifier
                .fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                uiState.menus?.forEach { menu ->
                    stickyHeader {
                        MenuHeader(menu = menu)
                    }
                    items(menu.items){ item ->
                        MenuItem(
                            menuItem = item,
                            onClick = { menuItem ->
                                viewModel.setMenu(menuItem)
                                showBottomSheet = true
                            }
                        )
                    }
                }
            }
        }
    }



    if (showBottomSheet) {
        uiState.currentMenu?.let {
            MenuItemModal(
                menuItem = it,
                onDismiss = {showBottomSheet = false}
            )
        }
    }

}


