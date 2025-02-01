package com.yehorsk.platea.core.presentation.settings.components

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yehorsk.platea.R
import com.yehorsk.platea.ui.theme.MobileTheme

@Composable
fun ProfileListItem(
    modifier: Modifier = Modifier,
    @StringRes text: Int,
    onClick: () -> Unit
){

    Row(
        modifier = modifier.background(MaterialTheme.colorScheme.background)
            .fillMaxWidth()
            .clickable {
                onClick()
            }
    ){
        Text(
            modifier = Modifier
                .padding(16.dp)
                .weight(2f),
            text = stringResource(text),
            fontSize = 18.sp,
        )
        Box(
            modifier = Modifier
                .padding(16.dp)
                .weight(1f),
            contentAlignment = Alignment.CenterEnd
        ){
            Icon(
                imageVector = Icons.Filled.ArrowForwardIos,
                contentDescription = ""
            )
        }
    }
    HorizontalDivider()
}

@Preview
@Composable
fun ProfileListItemPreview(){
    MobileTheme {
        ProfileListItem(
            text = R.string.favorites,
            onClick = {}
        )
    }
}