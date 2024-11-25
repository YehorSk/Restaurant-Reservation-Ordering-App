package com.example.mobile.menu.presentation.search

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mobile.core.EventConsumer
import com.example.mobile.core.domain.SideEffect
import com.example.mobile.menu.presentation.menu.components.MenuItem
import com.example.mobile.menu.presentation.menu.components.SearchBar
import com.example.mobile.menu.presentation.menu.viewmodel.MenuScreenViewModel
import com.example.mobile.ui.theme.MobileTheme

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    onGoBack: () -> Unit,
    viewModel: MenuScreenViewModel
){

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val searchUiState = viewModel.searchUiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    EventConsumer(channel = viewModel.sideEffect) { sideEffect ->
        when(sideEffect){
            is SideEffect.ShowToast -> Toast.makeText(context, sideEffect.message, Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = modifier
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primaryContainer)
                .height(85.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(Modifier.width(8.dp))
            Icon(
                modifier = Modifier
                    .size(30.dp)
                    .clickable {
                        onGoBack()
                    },
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = null
            )
            Spacer(Modifier.width(8.dp))
            SearchBar(
                onClick = {},
                onValueChange = {value -> viewModel.onSearchValueChange(value = value)},
                text = uiState.searchText,
                isConnected = true
            )
        }
        LazyColumn {
            items(searchUiState.value){ item ->
                MenuItem(
                    menuItem = item,
                    onClick = { menuItem ->
                        viewModel.setMenu(menuItem)
                        viewModel.updatePrice(menuItem.price.toDouble())
                        viewModel.setMenuItemId(menuItem.id)
                        viewModel.showBottomSheet()
                        onGoBack()
                    },
                    isConnected = true
                )
                HorizontalDivider()
            }
        }
    }
}

//@Composable
//@PreviewLightDark
//fun SearchScreenPreview(){
//    MobileTheme {
//        SearchScreen(
//            onGoBack = {}
//        )
//    }
//}