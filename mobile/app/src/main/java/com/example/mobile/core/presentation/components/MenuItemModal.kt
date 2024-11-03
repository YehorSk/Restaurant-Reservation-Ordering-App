package com.example.mobile.core.presentation.components

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.mobile.R
import com.example.mobile.menu.data.db.model.MenuItemEntity
import com.example.mobile.ui.theme.MobileTheme
import com.example.mobile.utils.formattedPrice
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuItemModal(
    onDismiss:()->Unit,
    menuItem: MenuItemEntity,
    modifier: Modifier = Modifier,
    cartForm: CartForm,
    onQuantityChange: (Int) -> Unit,
    onPriceChange: (Double) -> Unit,
    addUserCartItem: () -> Unit,
    addFavoriteItem: () -> Unit,
    deleteFavoriteItem: () -> Unit,
    showFavorite: Boolean = true,
    @StringRes buttonText: Int
){
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        dragHandle = null,
        content = {
            MenuItemModalContent(
                onDismiss,
                menuItem,
                cartForm = cartForm,
                onQuantityChange = onQuantityChange,
                onPriceChange = onPriceChange,
                addUserCartItem = addUserCartItem,
                buttonText = buttonText,
                addFavoriteItem = addFavoriteItem,
                deleteFavoriteItem = deleteFavoriteItem,
                showFavorite = showFavorite
            )
        }
    )
}

@Composable
fun MenuItemModalContent(
    onDismiss:()->Unit,
    menuItem: MenuItemEntity,
    modifier: Modifier = Modifier,
    cartForm: CartForm,
    onQuantityChange: (Int) -> Unit,
    onPriceChange: (Double) -> Unit,
    addUserCartItem: () -> Unit,
    addFavoriteItem: () -> Unit,
    deleteFavoriteItem: () -> Unit,
    showFavorite: Boolean = true,
    @StringRes buttonText: Int
){
    val contentColor = if(isSystemInDarkTheme()){
        Color.White
    }else{
        Color.Black
    }
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
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
                if(showFavorite){
                    IconButton(
                        onClick = {
                            if(cartForm.isFavorite) deleteFavoriteItem()
                            else addFavoriteItem()
                        },
                        modifier = Modifier.background(Color.White),
                    ) {
                        if(cartForm.isFavorite){
                            Icon(
                                imageVector = Icons.Filled.Favorite,
                                contentDescription = "",
                                modifier = Modifier
                                    .size(30.dp)
                            )
                        }else{
                            Icon(
                                imageVector = Icons.Filled.FavoriteBorder,
                                contentDescription = "",
                                modifier = Modifier
                                    .size(30.dp)
                            )
                        }
                    }
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
                textAlign = TextAlign.Center,
                color = contentColor
            )
            Text(
                modifier = Modifier.padding(top = 16.dp, start = 32.dp, end = 32.dp),
                fontSize = 16.sp,
                text = "€"+formattedPrice(cartForm.price),
                color = contentColor
            )
            Text(
                modifier = Modifier.padding(top = 8.dp, start = 32.dp, end = 32.dp),
                fontSize = 16.sp,
                text = menuItem.longDescription,
                color = contentColor
            )
            Spacer(
                modifier = Modifier
                    .padding(top = 5.dp, bottom = 5.dp)
                    .fillMaxWidth()
                    .height(10.dp)
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
                            onClick = {
                                if(cartForm.quantity>1){
                                    val newQuantity = cartForm.quantity - 1
                                    onQuantityChange(newQuantity)
                                    onPriceChange((menuItem.price.toDouble() * newQuantity).toDouble())
                                }
                            }
                        ) {
                            Text(
                                fontSize = 30.sp,
                                text = "-"
                            )
                        }
                        Text(
                            fontSize = 30.sp,
                            text = cartForm.quantity.toString()
                        )
                        TextButton(
                            shape = CircleShape,
                            onClick = {
                                val newQuantity = cartForm.quantity + 1
                                onQuantityChange(newQuantity)
                                onPriceChange((menuItem.price.toDouble() * newQuantity).toDouble())
                            }
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
                    onClick = {
                        addUserCartItem()
                    }
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            fontSize = 20.sp,
                            text = stringResource(buttonText)
                        )
                        Text(
                            fontSize = 20.sp,
                            text = "€"+ formattedPrice(cartForm.price)
                        )
                    }
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
fun PreviewMenuItemModal() {
    MobileTheme {
        val menuItem = MenuItemEntity(
            id = 1,
            createdAt = "2023-01-01",
            updatedAt = "2023-01-02",
            menuId = 123,
            name = "Delicious Pizza",
            shortDescription = "A short description of the pizza.",
            longDescription = "A delicious pizza topped with fresh ingredients, including tomatoes, cheese, and basil.",
            recipe = "Tomatoes, cheese, basil, dough",
            picture = "https://example.com/pizza.jpg",
            price = 9.99,
            isFavorite = false
        )

        MenuItemModalContent(
            onDismiss = {},
            menuItem = menuItem,
            onQuantityChange = {value -> },
            onPriceChange = {value -> },
            cartForm = CartForm(),
            addUserCartItem = {},
            buttonText = R.string.Add,
            deleteFavoriteItem = {},
            addFavoriteItem = {}
        )
    }
}
