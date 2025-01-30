package com.yehorsk.platea.menu.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yehorsk.platea.menu.data.db.model.MenuEntity

@Composable
fun MenuHeader(
    menuDto: MenuEntity,
    modifier: Modifier = Modifier,
    onMenuClick: () -> Unit
){
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primaryContainer)
            .clickable{
                onMenuClick()
            }
    ) {
        Text(
            text = menuDto.name,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 16.dp)
        )
        Text(
            text = menuDto.description,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontSize = 14.sp,
            modifier = modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
        )
    }

}
