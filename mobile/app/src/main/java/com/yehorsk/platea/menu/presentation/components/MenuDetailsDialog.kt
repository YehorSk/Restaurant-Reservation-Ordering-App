package com.yehorsk.platea.menu.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.yehorsk.platea.menu.domain.models.Menu
import com.yehorsk.platea.ui.theme.MobileTheme

@Composable
fun MenuDetailsDialog(
    modifier: Modifier = Modifier, 
    menu: Menu,
    onDismissRequest: () -> Unit
    ){
    Dialog(
        onDismissRequest = { onDismissRequest() }
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = menu.name,
                    modifier = Modifier
                        .padding(bottom = 10.dp)
                )
                HorizontalDivider()
                Text(
                    text = menu.description,
                    modifier = Modifier
                        .padding(top = 10.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun MenuDetailsDialogPreview(){
    MobileTheme { 
        MenuDetailsDialog(
            menu = Menu(
                id = "1",
                createdAt = "2024-09-25T10:00:00Z",
                updatedAt = "2024-09-25T12:30:00Z",
                name = "Seasonal Special Menu",
                description = "Enjoy a variety of our chef's signature dishes, featuring locally sourced ingredients and seasonal flavors. Perfect for any occasion, our special menu includes a mix of appetizers, mains, and desserts crafted to delight your taste buds.",
                availability = true,
                items = listOf()
            ),
            onDismissRequest = {}
        )
    }
}