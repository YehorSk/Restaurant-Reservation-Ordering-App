package com.yehorsk.platea.core.presentation.components

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.yehorsk.platea.BuildConfig
import com.yehorsk.platea.R
import com.yehorsk.platea.core.utils.formattedPrice
import com.yehorsk.platea.menu.data.db.model.MenuItemEntity
import com.yehorsk.platea.ui.theme.MobileTheme

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
    deleteCartItem: () -> Unit = {},
    showFavorite: Boolean = true,
    @StringRes buttonText: Int
){
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        modifier = modifier,
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        dragHandle = null,
        content = {
            MenuItemModalContent(
                onDismiss = onDismiss,
                menuItem = menuItem,
                cartForm = cartForm,
                onQuantityChange = onQuantityChange,
                onPriceChange = onPriceChange,
                addUserCartItem = addUserCartItem,
                buttonText = buttonText,
                addFavoriteItem = addFavoriteItem,
                deleteFavoriteItem = deleteFavoriteItem,
                showFavorite = showFavorite,
                deleteCartItem = deleteCartItem
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
    deleteCartItem: () -> Unit = {},
    showFavorite: Boolean = true,
    @StringRes buttonText: Int
){
    val imgUrl = BuildConfig.BASE_URL_IMG
    BoxWithConstraints {
        val widthModifier = if (this.maxWidth < 400.dp) {
            Modifier.fillMaxWidth()
        } else {
            Modifier.width(640.dp)
        }
        Column(
            modifier = modifier
                .then(widthModifier)
                .verticalScroll(rememberScrollState())
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
                    model = "$imgUrl${menuItem.picture}",
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
                    Card(
                        modifier = Modifier,
                        shape = RoundedCornerShape(40.dp),
                        colors = CardColors(
                            containerColor = MaterialTheme.colorScheme.tertiary,
                            contentColor = Color.White,
                            disabledContentColor = MaterialTheme.colorScheme.tertiary,
                            disabledContainerColor = MaterialTheme.colorScheme.tertiary
                        )
                    ) {
                        Row {
                            if(showFavorite){
                                var isFavorite = if (cartForm.isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder
                                IconButton(
                                    colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.tertiary),
                                    onClick = {
                                        if(cartForm.isFavorite){
                                            deleteFavoriteItem()
                                        } else {
                                            addFavoriteItem()
                                        }
                                        onDismiss()
                                    },
                                ) {
                                    Icon(
                                        imageVector = isFavorite,
                                        contentDescription = "",
                                        modifier = Modifier
                                            .size(30.dp),
                                        tint = Color.White
                                    )
                                }
                            }
                            IconButton(
                                onClick = onDismiss,
                                colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.tertiary),
                                ) {
                                Icon(
                                    imageVector = Icons.Filled.Close,
                                    contentDescription = "",
                                    modifier = Modifier.size(30.dp),
                                    tint = Color.White
                                )
                            }
                        }
                    }
                }
            }
            Column{
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    text = menuItem.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                )
                Text(
                    modifier = Modifier.padding(top = 16.dp, start = 32.dp, end = 32.dp),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    text = "€"+formattedPrice(menuItem.price),
                )
                Text(
                    modifier = Modifier.padding(top = 8.dp, start = 32.dp, end = 32.dp),
                    style = MaterialTheme.typography.titleMedium,
                    text = menuItem.shortDescription,
                )
                Spacer(
                    modifier = Modifier
                        .padding(top = 5.dp, bottom = 5.dp)
                        .fillMaxWidth()
                        .height(10.dp)
                )
                if(!showFavorite){
                    HorizontalDivider()
                    Row(
                        modifier = Modifier
                            .padding(start = 20.dp, top = 10.dp, bottom = 10.dp, end = 20.dp)
                            .fillMaxWidth()
                            .clickable{ deleteCartItem() }
                        ,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            tint = MaterialTheme.colorScheme.error,
                            contentDescription = "",
                            modifier = Modifier
                                .size(30.dp)
                        )
                        Text(
                            modifier = Modifier.padding(start = 10.dp),
                            text = stringResource(R.string.DELETE),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
                HorizontalDivider()
                Row(
                    modifier = Modifier
                        .height(IntrinsicSize.Min)
                        .padding(
                            start = 20.dp,
                            top = 10.dp,
                            bottom = 10.dp,
                            end = 20.dp
                        )
                ) {
                    if(menuItem.availability){
                        Card(
                            modifier = Modifier
                                .fillMaxHeight()
                                .wrapContentWidth(),
                            shape = RoundedCornerShape(40.dp),
                            colors = CardColors(
                                containerColor = MaterialTheme.colorScheme.tertiary,
                                contentColor = Color.White,
                                disabledContentColor = MaterialTheme.colorScheme.tertiary,
                                disabledContainerColor = MaterialTheme.colorScheme.tertiary
                            )
                        ) {
                            Row(
                                modifier = Modifier.fillMaxHeight(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceEvenly,
                            ){
                                TextButton(
                                    modifier = Modifier.fillMaxHeight(),
                                    shape = CircleShape,
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary),
                                    onClick = {
                                        if(cartForm.quantity>1){
                                            val newQuantity = cartForm.quantity - 1
                                            onQuantityChange(newQuantity)
                                            onPriceChange(menuItem.price * newQuantity)
                                        }
                                    }
                                ) {
                                    AutoResizedText(
                                        text = "-",
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        maxLines = 1
                                    )
                                }
                                AutoResizedText(
                                    text = cartForm.quantity.toString(),
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    maxLines = 1
                                )
                                TextButton(
                                    modifier = Modifier.fillMaxHeight(),
                                    shape = CircleShape,
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary),
                                    onClick = {
                                        val newQuantity = cartForm.quantity + 1
                                        onQuantityChange(newQuantity)
                                        onPriceChange(menuItem.price * newQuantity)
                                    }
                                ) {
                                    AutoResizedText(
                                        text = "+",
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        maxLines = 1
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.width(20.dp))
                        Button(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary),
                            onClick = {
                                addUserCartItem()
                            }
                        ) {
                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                AutoResizedText(
                                    text = stringResource(buttonText),
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    maxLines = 1
                                )
                                AutoResizedText(
                                    text = "€"+ formattedPrice(cartForm.price),
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    maxLines = 1
                                )
                            }
                        }
                    }else{
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth()
                                .wrapContentSize(Alignment.Center),
                        ){
                            AutoResizedText(
                                text = stringResource(R.string.not_available),
                                style = MaterialTheme.typography.headlineLarge,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewMenuItemModal() {
    MobileTheme {
        val menuItem = MenuItemEntity(
            id = 1,
            createdAt = "2023-01-01",
            updatedAt = "2023-01-02",
            menuId = "123",
            name = "Delicious Pizza",
            shortDescription = "A short description of the pizza.",
            longDescription = "A delicious pizza topped with fresh ingredients, including tomatoes, cheese, and basil.",
            recipe = "Tomatoes, cheese, basil, dough",
            picture = "https://example.com/pizza.jpg",
            price = 10.9,
            isFavorite = false,
            availability = true
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
