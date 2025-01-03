package com.example.mobile.orders.presentation.components

import android.graphics.drawable.Icon
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobile.ui.theme.MobileTheme
import com.example.mobile.R

@Composable
fun OrderAddMore(
    modifier: Modifier = Modifier,
    onGoToCart: () -> Unit,
    onGoToMenu: () -> Unit,
){
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
    ){
        OrderButton(
            icon = Icons.Filled.AddShoppingCart,
            text = R.string.add_to_cart,
        ) {
            onGoToMenu()
        }
        HorizontalDivider()
        OrderButton(
            icon = Icons.Filled.ShoppingCart,
            text = R.string.go_to_cart
        ) {
            onGoToCart()
        }
    }
}

@Composable
fun OrderButton(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    @StringRes text: Int,
    onClick: () -> Unit
){
    val contentColor = if(isSystemInDarkTheme()){
        Color.White
    }else{
        Color.Black
    }

    Row(
        modifier = modifier
            .clickable{
                onClick()
            }
            .background(MaterialTheme.colorScheme.background)
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
            imageVector = icon,
            contentDescription = "",
            tint = contentColor,
        )
        Text(
            modifier = Modifier
                .padding(
                    start = 10.dp,
                    top = 20.dp,
                    bottom = 20.dp
                ),
            text = stringResource(id = text),
            color = contentColor,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}

@PreviewLightDark
@Composable
fun OrderAddMorePreview(){
    MobileTheme {
        OrderAddMore(
            onGoToCart = {},
            onGoToMenu = {}
        )
    }
}