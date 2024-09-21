package com.example.mobile.ui.screens.client.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.mobile.R
import com.example.mobile.ui.screens.client.home.components.MenuHeader
import com.example.mobile.ui.screens.client.home.components.MenuItem
import com.example.mobile.ui.screens.client.home.components.MenuItemModal
import com.example.mobile.ui.screens.client.home.viewmodel.CleintMainScreenViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: CleintMainScreenViewModel = hiltViewModel()
){

    val uiState by viewModel.uiState.collectAsState()
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxSize()
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

    if (showBottomSheet) {
        uiState.currentMenu?.let {
            MenuItemModal(
                menuItem = it,
                onDismiss = {showBottomSheet = false}
            )
        }
    }

}


