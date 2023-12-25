package com.example.flowershopapp.ComposeUI.Order

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.example.flowershopapp.ComposeUI.AppViewModelProvider
import com.example.flowershopapp.ComposeUI.Navigation.Screen
import com.example.flowershopapp.ComposeUI.User.OrderViewModel
import com.example.flowershopapp.Entities.Model.Order
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Orders(
    navController: NavController,
    viewModel: OrderViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {


    val ordersListUiState = viewModel.ordersListUiState.collectAsLazyPagingItems()
    val refreshScope = rememberCoroutineScope()
    var refreshing by remember { mutableStateOf(false) }
    fun refresh() = refreshScope.launch {
        refreshing = true
        ordersListUiState?.refresh()
        refreshing = false
    }

    val state = rememberPullRefreshState(refreshing, ::refresh)
    LazyColumn(
        modifier = Modifier
            .pullRefresh(state)
            .padding(top = 16.dp)
    ) {
        items(
            count = ordersListUiState.itemCount,
            key = ordersListUiState.itemKey(),
            contentType = ordersListUiState.itemContentType()
        ) { index ->
            val order = ordersListUiState[index]
            order?.let {
                OrderItem(order){
                    navController.navigate(
                        Screen.OrderBouquets.route.replace(
                            "{id}",
                            order.orderId.toString()
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun OrderItem(order: Order, action: () -> Unit) {
    Card(

        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp,
            focusedElevation = 20.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 5.dp, bottom = 20.dp, start = 30.dp, end = 30.dp)
            .clickable {
                action()
            },
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Номер заказа: ${order.orderId}",
                fontWeight = FontWeight.Bold
            )
            Text(text = "Дата: ${order.date}")
            Text(text = "Сумма: ${order.sum}")
        }
    }
}