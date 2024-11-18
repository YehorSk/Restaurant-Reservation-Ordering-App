package com.example.mobile.orders.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobile.ui.theme.MobileTheme

@Composable
fun OrderAddMore(
    modifier: Modifier = Modifier
){
    Row(
        modifier = modifier
            .background(Color.White)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Icon(
            modifier = Modifier
                .padding(
                    start = 10.dp,
                    top = 20.dp,
                    bottom = 20.dp
                ),
            imageVector = Icons.Filled.Add,
            contentDescription = "",
            tint = MaterialTheme.colorScheme.primary,
        )
        Text(
            modifier = Modifier
                .padding(
                    start = 10.dp,
                    top = 20.dp,
                    bottom = 20.dp
                ),
            text = "Add more",
            color = MaterialTheme.colorScheme.primary,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Preview
@Composable
fun OrderAddMorePreview(){
    MobileTheme {
        OrderAddMore()
    }
}