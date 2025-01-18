package com.example.mobile.core.presentation.settings.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mobile.ui.theme.MobileTheme

@Composable
fun ProfileListHeader(
    modifier: Modifier = Modifier,
    text: String
){

    Row(
        modifier = modifier.background(MaterialTheme.colorScheme.background)
            .fillMaxWidth()
    ){
        Text(
            modifier = Modifier
                .padding(16.dp)
                .weight(1f),
            text = text,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.ExtraBold
        )
    }

}

@Preview
@Composable
fun ProfileListHeaderPreview(){
    MobileTheme {
        ProfileListHeader(
            text = "Hello User"
        )
    }
}