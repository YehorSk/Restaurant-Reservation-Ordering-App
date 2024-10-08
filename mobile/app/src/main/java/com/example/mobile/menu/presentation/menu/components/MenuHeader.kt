package com.example.mobile.menu.presentation.menu.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobile.menu.data.remote.model.Menu

@Composable
fun MenuHeader(
    menu: Menu,
    modifier: Modifier = Modifier
){
    Column(
        modifier = Modifier.background(MaterialTheme.colorScheme.primaryContainer)
    ) {
        Text(
            text = menu.name,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 16.dp)
        )
        Text(
            text = menu.description,
            maxLines = 1,
            fontSize = 14.sp,
            modifier = modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
        )
    }

}
