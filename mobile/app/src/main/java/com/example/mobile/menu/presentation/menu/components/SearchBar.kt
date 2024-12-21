package com.example.mobile.menu.presentation.menu.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.example.mobile.ui.theme.MobileTheme

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true,
    text: String = "",
    onValueChange: (String) -> Unit,
    isConnected: Boolean
){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ){
        OutlinedTextField(
            modifier = modifier
                .fillMaxWidth()
                .background(Color.White)
                .clickable { onClick() },
            value = text,
            onValueChange = onValueChange,
            enabled = enabled,
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Search Icon") },
            placeholder = {
                Text(
                    text = "Search"
                )
            }
        )
    }
}

@PreviewLightDark
@Composable
fun SearchBarPreview(){
    MobileTheme {
        SearchBar(
            onClick = {},
            onValueChange = {},
            isConnected = true
        )
    }
}