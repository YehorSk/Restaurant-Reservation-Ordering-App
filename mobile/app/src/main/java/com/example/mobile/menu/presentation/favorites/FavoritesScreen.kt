package com.example.mobile.menu.presentation.favorites

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mobile.menu.presentation.menu.components.MenuItem
import com.example.mobile.menu.presentation.menu.viewmodel.MenuScreenViewModel

@Composable
fun FavoritesScreen(
    modifier: Modifier = Modifier,
    viewModel: MenuScreenViewModel
){

    val favoriteUiState by viewModel.favoriteUiState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(
                items = favoriteUiState,
                key = { it.id }
            ){ item ->
                MenuItem(
                    menuItem = item,
                    onClick = { menuItem ->

                    }
                )
                HorizontalDivider()
            }

        }
    }
}