package com.example.flowershopapp.Entities.ComposeUI

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.flowershopapp.ComposeUI.AppViewModelProvider
import com.example.flowershopapp.ComposeUI.Bouquet.BouquetCard
import com.example.flowershopapp.ComposeUI.Bouquet.BouquetCatalogViewModel
import com.example.flowershopapp.ComposeUI.User.OrderViewModel
import com.example.flowershopapp.Entities.Model.AuthModel
import com.example.flowershopapp.Entities.Model.Bouquet
import com.example.flowershopapp.Entities.Model.CartModel
import com.example.flowershopapp.Entities.Model.FavoriteModel
import com.example.flowershopapp.Entities.Model.Order
import com.example.flowershopapp.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun ShoppingCart(
    navController: NavController,
    viewModel: OrderViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val bouquets by CartModel.instance.bouquets.collectAsState()
    val totalSum by CartModel.instance.totalSum.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally,) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Корзина", fontFamily = FontFamily.Serif, fontSize = 40.sp, fontWeight = FontWeight.W600)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Text(text = "Общая сумма: ${totalSum}", fontSize = 25.sp, fontWeight = FontWeight.Bold)
            Button(
                onClick = {
                    val currentDate = LocalDate.now()
                    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                    val formattedDate = currentDate.format(formatter)
                    val order = Order(date = formattedDate, sum = totalSum, userId = AuthModel.currentUser.userId!!)
                    viewModel.createOrder(order, bouquets.toMutableList())
                    showDialog = true
                },
                modifier = Modifier.padding(bottom = 20.dp),
                shape = RoundedCornerShape(5.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.button)
                )
            ) {
                Text("Оформить")
            }
        }
        if (showDialog) {
            AlertDialog(
                onDismissRequest = {
                    CartModel.instance.clearBouquets()
                    showDialog = false
                },
                title = {
                    Text(text = "Заказ сделан!")
                },
                text = {
                    Text("Спасибо за покупку!")
                },
                confirmButton = {
                    Button(
                        onClick = {
                            CartModel.instance.clearBouquets()
                            showDialog = false
                        }
                    ) {
                        Text("Ок")
                    }
                }
            )
        }
        val padding = if (bouquets.size == 1) 95.dp else 0.dp
        LazyVerticalGrid(
            columns = GridCells.Fixed(if (bouquets.size == 1) 1 else 2 ),
            contentPadding = PaddingValues(start = padding, end = padding),
            modifier = Modifier.fillMaxSize()
        ) {
            items(bouquets) { bouquet ->
                CartBouquetCard(bouquet)
            }
        }
    }
}

@Composable
fun CartBouquetCard(bouquetPair: Pair<Bouquet, Int>){
    val bouquet = bouquetPair.first
    val countBouquet = bouquetPair.second
    var heart by remember { mutableStateOf(FavoriteModel.instance.containsBouquet(bouquet)) }
    var cart by remember { mutableStateOf(CartModel.instance.containsBouquet(bouquet)) }
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
            //.height(340.dp)
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box() {
                bouquet.image?.let { imageData ->
                    val decodedBitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.size)
                    val imageBitmap = decodedBitmap.asImageBitmap()
                    Image(
                        bitmap = imageBitmap,
                        contentDescription = null,
                        modifier = Modifier
                            .height(190.dp)
                            //.width(210.dp)
                            .clip(shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)),
                        contentScale = ContentScale.FillBounds
                    )
                }
                Box(
                    Modifier
                        .padding(start = 3.dp, top = 3.dp)
                        .align(Alignment.TopStart)
                ) {
                    Icon(
                        painter = painterResource(id = cart),
                        contentDescription = null,
                        modifier = Modifier
                            .size(30.dp)
                            .clip(shape = RoundedCornerShape(5.dp))
                            .clickable {
                                cart = CartModel.instance.updateList(bouquet)
                            }
                    )
                }
                Box(
                    Modifier
                        .padding(end = 3.dp, top = 3.dp)
                        .align(Alignment.TopEnd)
                ) {
                    Icon(
                        painter = painterResource(id = heart),
                        contentDescription = null,
                        modifier = Modifier
                            .size(30.dp)
                            .clip(shape = RoundedCornerShape(5.dp))
                            .clickable {
                                heart = FavoriteModel.instance.addBouquet(bouquet)
                            }
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = bouquet.name, fontFamily = FontFamily.Serif, fontSize = 20.sp)
            Text(text = "${bouquet.quantityOfFlowers} цветов", fontFamily = FontFamily.Serif, fontSize = 15.sp)
            Text(text = "${bouquet.price}", fontFamily = FontFamily.Serif, fontSize = 15.sp)
            Spacer(modifier = Modifier.height(5.dp))
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceAround, modifier = Modifier.height(50.dp)) {
                Box(modifier = Modifier.fillMaxHeight(0.5f)){
                    Icon(
                        painter = painterResource(id = R.drawable.icon_minus_cirlce),
                        contentDescription = "",
                        modifier = Modifier.clickable {
                            CartModel.instance.updateCount(bouquet, false)
                        }
                    )
                }
                Box(){
                    Text(text = "${countBouquet}")
                }
                Box(modifier = Modifier.fillMaxHeight(0.5f)){
                    Icon(
                        painter = painterResource(id = R.drawable.icon_add_circle),
                        contentDescription = "",
                        modifier = Modifier.clickable {
                            CartModel.instance.updateCount(bouquet, true)
                        }
                    )
                }
            }
        }
    }
}