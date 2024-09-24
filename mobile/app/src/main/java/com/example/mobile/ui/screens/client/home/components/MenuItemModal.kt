package com.example.mobile.ui.screens.client.home.components

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
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
    var amount by remember{ mutableStateOf(1) }
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
                Column{
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
                    Row(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Card(
                            modifier = Modifier
                                .wrapContentWidth()
                                .height(66.dp),
                            shape = RoundedCornerShape(40.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxHeight(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceEvenly,
                            ){
                                TextButton(
                                    shape = CircleShape,
                                    onClick = {if(amount>1) amount--}
                                ) {
                                    Text(
                                        fontSize = 30.sp,
                                        text = "-"
                                    )
                                }
                                Text(
                                    fontSize = 30.sp,
                                    text = amount.toString()
                                )
                                TextButton(
                                    shape = CircleShape,
                                    onClick = {amount++}
                                ) {
                                    Text(
                                        fontSize = 30.sp,
                                        text = "+"
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.width(20.dp))
                        Button(
                            modifier = Modifier.fillMaxWidth().height(66.dp),
                            onClick = {}
                        ) {
                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    fontSize = 20.sp,
                                    text = "Add"
                                )
                                Text(
                                    fontSize = 20.sp,
                                    text = "€"+String.format("%.2f", (menuItem.price.toFloat()*amount))
                                )
                            }
                        }
                    }
                }
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
