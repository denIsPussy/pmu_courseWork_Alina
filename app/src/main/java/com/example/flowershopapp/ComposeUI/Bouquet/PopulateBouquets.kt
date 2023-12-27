package com.example.flowershopapp.ComposeUI.Bouquet

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.flowershopapp.API.APIStatus
import com.example.flowershopapp.ComposeUI.AppViewModelProvider
import com.example.flowershopapp.ComposeUI.Navigation.Screen
import com.example.flowershopapp.ComposeUI.Network.ErrorPlaceholder
import com.example.flowershopapp.ComposeUI.Network.LoadingPlaceholder

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PopulateBouquets(
    navController: NavController,
    viewModel: BouquetCatalogViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    if (viewModel.apiStatus == APIStatus.ERROR) {
        ErrorPlaceholder(
            message = viewModel.apiError,
            onBack = { navController.navigate(Screen.Profile.route) }
        )
        return
    }
    LaunchedEffect(Unit){
        viewModel.collectPopulateBouquets()
    }
    var bouquetListUiState = viewModel.bouquetPopulateListUiState
    when (viewModel.apiStatus) {
        APIStatus.DONE -> {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Хиты продаж",
                    fontFamily = FontFamily.Serif,
                    fontSize = 40.sp,
                    fontWeight = FontWeight.W600
                )
                Text(
                    text = "Топ-5 продаваемых букетов",
                    fontFamily = FontFamily.Serif,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.W400
                )
                val padding = if (bouquetListUiState.size == 1) 95.dp else 0.dp
                LazyVerticalGrid(
                    columns = GridCells.Fixed(if (bouquetListUiState.size == 1) 1 else 2),
                    contentPadding = PaddingValues(start = padding, end = padding)
                ) {
                    items(items = bouquetListUiState) { bouquet ->
                        BouquetCard(bouquet = bouquet)
                    }
                }
            }
        }
        APIStatus.LOADING -> LoadingPlaceholder()
        else -> ErrorPlaceholder(
            message = viewModel.apiError,
            onBack = { navController.navigate(Screen.Profile.route) }
        )
    }
}