package com.example.flowershopapp.ComposeUI.User

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.example.flowershopapp.ComposeUI.AppViewModelProvider
import com.example.flowershopapp.ComposeUI.Navigation.Screen
import com.example.flowershopapp.ComposeUI.Order.OrderItem
import com.example.flowershopapp.Entities.Model.OrderByDate
import com.example.flowershopapp.R
import java.util.Calendar

@Composable
fun Statistics(
    navController: NavController,
    viewModel: OrderViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {


    var startDateError by remember { mutableStateOf<String?>(null) }
    var endDateError by remember { mutableStateOf<String?>(null) }
    val orderListUiState = viewModel.orderByDateListUiState.collectAsState(OrderByDate(listOf(), 0, 0))

    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    var startDate by remember { mutableStateOf("Выберите начальную дату") }
    val startDateDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            startDate = "$year-${month + 1}-$dayOfMonth"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    var endDate by remember { mutableStateOf("Выберите конечную дату") }
    val endDateDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            endDate = "$year-${month + 1}-$dayOfMonth"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    fun validateInput(): Boolean {
        var isValid = true

        if (startDate.isBlank()) {
            startDateError = "Введите начальную дату"
            isValid = false
        } else {
            startDateError = null
        }

        if (endDate.isBlank()) {
            endDateError = "Введите конечную дату"
            isValid = false
        } else {
            endDateError = null
        }

        return isValid
    }

    fun getStatistics() {
        if (validateInput()) {
            viewModel.loadStatistics(startDate, endDate)
        }
    }

    Column(
        modifier = Modifier
            //.fillMaxSize()
            .background(color = colorResource(id = R.color.backgroundWindow)),
        verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, bottom = 10.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Статистика по заказам",
                fontSize = 32.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 50.dp, end = 50.dp),
                shape = RoundedCornerShape(5.dp),
                onClick = { startDateDialog.show() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.button)
                )
            ) {
                Text(startDate)
            }
            if (startDateError != null) {
                Text(text = startDateError!!, color = Color.Red)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 50.dp, end = 50.dp),
                shape = RoundedCornerShape(5.dp),
                onClick = { endDateDialog.show() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.button)
                )
            ) {
                Text(endDate)
            }
            if (endDateError != null) {
                Text(text = endDateError!!, color = Color.Red)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                shape = RoundedCornerShape(5.dp), onClick = { getStatistics() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.button)
                )
            ) {
                Text("Получить данные")
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, bottom = 10.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Всего заказов: ${orderListUiState.value.orderCount}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium
                )

                Text(
                    text = "Общая сумма: ${orderListUiState.value.totalSum}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
        Text(
            text = "Заказы",
            fontSize = 32.sp,
            fontWeight = FontWeight.Medium
        )
        LazyColumn(
            modifier = Modifier
                .padding(top = 16.dp)
        ) {
            items(
                items = orderListUiState.value.orders
            ) { order ->
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
