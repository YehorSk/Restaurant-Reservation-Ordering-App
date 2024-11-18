package com.example.mobile.orders.presentation.create_order

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mobile.R
import com.example.mobile.orders.presentation.components.NavBar
import com.example.mobile.ui.theme.MobileTheme
import com.example.mobile.orders.presentation.components.OrderOptions

@Composable
fun CreateOrderScreen(
    modifier: Modifier = Modifier,
    onGoBack: () -> Unit,
){
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        NavBar(
            onGoBack = onGoBack,
            title = R.string.complete_order_navbar_title
        )
        OrderOptions()
    }
}

@Composable
fun OrderSpecialRequest(){

}


@Preview
@Composable
fun CreateOrderScreenPreview(
    modifier: Modifier = Modifier
){
    MobileTheme {
        CreateOrderScreen {

        }
    }
}