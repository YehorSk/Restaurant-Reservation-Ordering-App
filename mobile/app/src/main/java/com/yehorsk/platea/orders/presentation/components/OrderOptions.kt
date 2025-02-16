package com.yehorsk.platea.orders.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeliveryDining
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.yehorsk.platea.R
import com.yehorsk.platea.ui.theme.MobileTheme

data class OrderOption(
    val  id: Int,
    val icon: ImageVector,
    val text: Int,
    val subText: Int
)

@Composable
fun OrderOptions(
    modifier: Modifier = Modifier,
    selected: Int,
    selectedTime: String,
    onSelectedChange: (Int,String) -> Unit
){


    val options = listOf<OrderOption>(
        OrderOption(id = 0, icon = Icons.Filled.DirectionsWalk, text = R.string.pickup_option, subText = R.string.pickup_time),
        OrderOption(id = 1, icon = Icons.Filled.DeliveryDining, text = R.string.delivery_option, subText = R.string.delivery_time),
        OrderOption(id = 2, icon = Icons.Filled.Restaurant, text = R.string.reservation_option, subText = R.string.select_date),
        OrderOption(id = 3, icon = Icons.Filled.Schedule, text = R.string.schedule, subText = R.string.select_time),
    )
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(options[selected] ) }
    Card(
    ) {
        Column(
            modifier = modifier
                .background(MaterialTheme.colorScheme.background)
        ) {
            Text(
                modifier = Modifier
                    .padding(
                        start = 20.dp,
                        top = 15.dp,
                        bottom = 10.dp
                    ),
                text = stringResource(R.string.order_preferences),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
            )
            for(option in options){
                val text = stringResource(option.text)
                OrderRadioOption(
                    option = option,
                    selected = (option.text == selectedOption.text),
                    onSelect = { option ->
                        if(option.id != 3){
                            onOptionSelected(option)
                        }
                        onSelectedChange(
                            option.id,
                            text
                        )
                    },
                    selectedTime = selectedTime
                )
                if(option.id !=3) HorizontalDivider()
            }
        }
    }
}

@Composable
fun OrderRadioOption(
    modifier: Modifier = Modifier,
    selected : Boolean = false,
    selectedTime: String,
    onSelect: (OrderOption) -> Unit,
    option: OrderOption
){

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
            .clickable {
                onSelect(option)
            }
        ,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ){
            Icon(
                modifier = Modifier
                    .padding(start = 10.dp),
                imageVector = option.icon,
                contentDescription = ""
            )
            Column {
                Text(
                    modifier = Modifier
                        .padding(start = 10.dp),
                    text = stringResource(id = option.text),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    style = LocalTextStyle.current.merge(
                        TextStyle(
                            lineHeightStyle = LineHeightStyle(
                                alignment = LineHeightStyle.Alignment.Center,
                                trim = LineHeightStyle.Trim.None
                            ),
                            platformStyle = PlatformTextStyle(
                                includeFontPadding = false
                            ),
                            lineHeight = 1.5.em,
                        )
                    )
                )
                Text(
                    modifier = Modifier
                        .padding(start = 10.dp),
                    text = if(selected && selectedTime.length > 5 && option.id != 2)
                                selectedTime
                            else
                                stringResource(id = option.subText),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    style = LocalTextStyle.current.merge(
                        TextStyle(
                            lineHeightStyle = LineHeightStyle(
                                alignment = LineHeightStyle.Alignment.Center,
                                trim = LineHeightStyle.Trim.None
                            ),
                            platformStyle = PlatformTextStyle(
                                includeFontPadding = false
                            ),
                            lineHeight = 1.5.em,
                        )
                    )
                )
            }
        }
        RadioButton(
            selected = selected,
            onClick = {
                onSelect(option)
            }
        )
    }
}

@Preview
@Composable
fun OrderOptionsPreview(){
    MobileTheme {
        OrderOptions(
            selected = 0,
            onSelectedChange = {
                id,text ->
            },
            selectedTime = ""
        )
    }
}

@PreviewLightDark
@Composable
fun OrderRadioOptionPreview(){
    MobileTheme {
        OrderRadioOption(
            option = OrderOption(
                id = 0,
                icon = Icons.Filled.DirectionsWalk,
                text = R.string.pickup_option,
                subText = R.string.pickup_time
            ),
            onSelect = {},
            selectedTime = ""
        )
    }
}