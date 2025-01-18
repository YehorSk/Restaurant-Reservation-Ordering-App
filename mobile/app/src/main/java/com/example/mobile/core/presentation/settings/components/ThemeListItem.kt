package com.example.mobile.core.presentation.settings.components

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ThemeListItem(
    modifier: Modifier = Modifier,
    @StringRes text: Int,
    onClick: () -> Unit,
    isActive: Boolean
){
    Row(
        modifier = modifier.background(MaterialTheme.colorScheme.background)
            .fillMaxWidth()
            .clickable {
                onClick()
            }
    ){
        Box(
            modifier = Modifier
                .padding(16.dp)
        ){
            if(isActive) {
                Icon(
                    imageVector = Icons.Filled.RadioButtonChecked,
                    contentDescription = ""
                )
            }else{
                Icon(
                    imageVector = Icons.Filled.RadioButtonUnchecked,
                    contentDescription = ""
                )
            }
        }
        Text(
            modifier = Modifier
                .padding(16.dp),
            text = stringResource(text),
            fontSize = 18.sp,
        )
    }
    HorizontalDivider()
}