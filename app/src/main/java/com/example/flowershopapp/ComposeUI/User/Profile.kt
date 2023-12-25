package com.example.flowershopapp.ComposeUI.User

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.flowershopapp.ComposeUI.Navigation.Screen
import com.example.flowershopapp.Entities.Model.AuthModel
import com.example.flowershopapp.Entities.Model.User
import com.example.flowershopapp.R


@Composable
fun Profile(navController: NavController) {
    var phoneNumber by remember { mutableStateOf("Ваш номер телефона") }
    var userName by remember { mutableStateOf("Имя пользователя") }
    var dateOfBirth by remember { mutableStateOf("01-01-2000") }
    val (user, setUser) = remember { mutableStateOf<User?>(null) }

    LaunchedEffect(Unit) {
        setUser(AuthModel.currentUser)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.backgroundWindow)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        user?.let {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(2f)
                    .padding(bottom = 10.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Профиль",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(16.dp))
                Image(
                    painter = painterResource(id = R.drawable.icons8_profile),
                    contentDescription = "",
                    Modifier.size(140.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                TextField(
                    value = user.userName,
                    onValueChange = { userName = it },
                    label = { Text("Имя пользователя") },
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = colorResource(id = R.color.textFieldContainer),
                        focusedContainerColor = colorResource(id = R.color.textFieldContainer)
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = user.dateOfBirth,
                    onValueChange = { dateOfBirth = it },
                    label = { Text("Дата рождения") },
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = colorResource(id = R.color.textFieldContainer),
                        focusedContainerColor = colorResource(id = R.color.textFieldContainer)
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = user.phoneNumber,
                    onValueChange = { phoneNumber = it },
                    label = { Text("Номер телефона") },
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = colorResource(id = R.color.textFieldContainer),
                        focusedContainerColor = colorResource(id = R.color.textFieldContainer)
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 90.dp, end = 90.dp),
                    shape = RoundedCornerShape(5.dp),
                    onClick = { navController.navigate(Screen.Orders.route) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.button)
                    )
                ) {
                    Text("История заказов")
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 90.dp, end = 90.dp),
                    shape = RoundedCornerShape(5.dp),
                    onClick = { navController.navigate(Screen.Statistics.route) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.button)
                    )
                ) {
                    Text("Статистика")
                }
            }
        }
    }

}