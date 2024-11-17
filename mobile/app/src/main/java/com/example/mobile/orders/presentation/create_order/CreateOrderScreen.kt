package com.example.mobile.orders.presentation.create_order

import androidx.annotation.StringRes
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mobile.ui.theme.MobileTheme
import com.example.mobile.R

@Composable
fun CreateOrderScreen(
    modifier: Modifier = Modifier,
    onGoBack: () -> Unit,
){
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primaryContainer)
                .height(60.dp),
            horizontalArrangement = Arrangement.Start,
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
        }
    }
}

@Composable
fun OrderOptions(
    modifier: Modifier = Modifier
){

}

@Composable
fun OrderRadioOption(
    icon: ImageVector,
    @StringRes text: Int
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(color = Color.White)
            .padding(10.dp)
        ,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ){
            Icon(
                modifier = Modifier
                    .padding(start = 10.dp),
                imageVector = icon,
                contentDescription = ""
            )
            Text(
                modifier = Modifier
                    .padding(start = 10.dp),
                text = stringResource(id = text)
            )
        }
        RadioButton(
            selected = true,
            onClick = {}
        )
    }
}

@Preview
@Composable
fun OrderRadioOptionPreview(){
    MobileTheme {
        OrderRadioOption(
            icon = Icons.Filled.DirectionsWalk,
            text = R.string.pickup_option
        )
    }
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