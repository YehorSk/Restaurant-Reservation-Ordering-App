package com.example.mobile.orders.presentation.order_details.components

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobile.R
import com.example.mobile.ui.theme.MobileTheme

@Composable
fun DeliveryDetails(
    modifier: Modifier = Modifier,
    address: String,
    instructions: String
){
    val contentColor = if(isSystemInDarkTheme()){
        Color.White
    }else{
        Color.Black
    }
    Card(
    ) {
        Column(
            modifier = modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxWidth()
        ) {
            Text(
                modifier = Modifier
                    .padding(
                        start = 20.dp,
                        top = 15.dp,
                        bottom = 10.dp
                    ),
                text = stringResource(R.string.delivery_details),
                color = contentColor,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
            )
            Text(
                modifier = Modifier
                    .padding(
                        start = 20.dp,
                        bottom = 10.dp
                    ),
                text = address,
                color = contentColor,
                fontSize = 16.sp,
            )
            Text(
                modifier = Modifier
                    .padding(
                        start = 20.dp,
                        bottom = 10.dp
                    ),
                text = instructions,
                color = contentColor,
                fontSize = 16.sp
            )
        }
    }
}

@PreviewLightDark
@Composable
fun DeliveryDetailsPreview(){
    MobileTheme {
        DeliveryDetails(
            address = "Banicova 3, Nitra 949 11",
            instructions = "Call me"
        )
    }
}