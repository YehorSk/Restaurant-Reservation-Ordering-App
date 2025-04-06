package com.yehorsk.platea.reservations.presentation.create_reservation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yehorsk.platea.R
import com.yehorsk.platea.ui.theme.MobileTheme

@Composable
fun PartySize(
    modifier: Modifier = Modifier,
    partySize: Int,
    onPartySizeChanged:(Int) -> Unit,
    maxTableSize: Int = 0
){

    val items = (1..maxTableSize).toList()

    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        Text(
            modifier = Modifier
                .padding(
                    bottom = 10.dp
                ),
            text = stringResource(R.string.party_size),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
        )
        Spacer(
            modifier = Modifier.height(20.dp)
        )
        LazyRow(
            modifier = modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items){ item ->
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .border(
                            width = 2.dp,
                            color = if (item == partySize) MaterialTheme.colorScheme.tertiary else Color.LightGray,
                            shape = CircleShape)
                        .size(50.dp)
                        .background(MaterialTheme.colorScheme.background)
                        .clickable{
                            onPartySizeChanged(item.toInt())
                        },
                    contentAlignment = Alignment.Center
                ){
                    Text(
                        modifier = Modifier.padding(10.dp),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        text = item.toString()
                    )
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
fun PartySizePreview(){
    MobileTheme {
        PartySize(
            onPartySizeChanged = {},
            partySize = 1
        )
    }
}