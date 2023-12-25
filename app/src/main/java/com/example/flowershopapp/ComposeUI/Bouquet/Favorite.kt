package com.example.flowershopapp.ComposeUI.Bouquet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.flowershopapp.Entities.Model.FavoriteModel

@Composable
fun Favorite(navController: NavController) {
    val bouquets by FavoriteModel.instance.bouquets.collectAsState()
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Избранное", fontFamily = FontFamily.Serif, fontSize = 40.sp,fontWeight = FontWeight.W600)
        val padding = if (bouquets.size == 1) 95.dp else 0.dp
        LazyVerticalGrid(
            columns = GridCells.Fixed(if (bouquets.size == 1) 1 else 2 ),
            contentPadding = PaddingValues(start = padding, end = padding),
            modifier = Modifier.fillMaxSize(),
        ) {
            items(bouquets) { bouquet->
                BouquetCard(bouquet = bouquet)
            }
        }
    }
}