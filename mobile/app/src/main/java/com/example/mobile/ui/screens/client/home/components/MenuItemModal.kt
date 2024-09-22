package com.example.mobile.ui.screens.client.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.mobile.R
import com.example.mobile.menu.data.model.MenuItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuItemModal(
    onDismiss:()->Unit,
    menuItem: MenuItem
){
    val sheetState = rememberModalBottomSheetState()
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        dragHandle = null,
        content = {
            Column(
                modifier = Modifier.background(Color.White),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                ){
                    AsyncImage(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp),
                        model = menuItem.picture,
                        contentDescription = "",
                        placeholder = painterResource(R.drawable.menu_item_placeholder),
                        contentScale = ContentScale.Crop,
                        error = painterResource(R.drawable.menu_item_placeholder)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalArrangement = Arrangement.End
                    ){
                        IconButton(
                            onClick = {},
                            modifier = Modifier.background(Color.White),
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Star,
                                contentDescription = "",
                                modifier = Modifier.size(30.dp)
                            )
                        }
                        IconButton(
                            onClick = onDismiss
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = "",
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    }
                }
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    text = menuItem.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Text(
                    modifier = Modifier.padding(top = 16.dp, start = 32.dp, end = 32.dp),
                    fontSize = 16.sp,
                    text = "€"+menuItem.price
                )
                Text(
                    modifier = Modifier.padding(top = 8.dp, start = 32.dp, end = 32.dp),
                    fontSize = 16.sp,
                    text = menuItem.longDescription
                )
            }
        }
    )
}

//@Preview(showBackground = true)
//@Composable
//fun PreviewMenuItemModal() {
//    MenuItemModal(
//        onDismiss = {},
//        menuItem = MenuItem(
//            id = "1",
//            createdAt = "2023-09-22",
//            updatedAt = "2023-09-22",
//            menuId = "menu1",
//            name = "Sample Dish",
//            description = "A delicious sample dish with fresh ingredients.",
//            picture = "https://example.com/sample.jpg",
//            price = "12.99"
//        )
//    )
//}
