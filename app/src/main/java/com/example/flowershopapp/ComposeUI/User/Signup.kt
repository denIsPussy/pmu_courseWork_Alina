package com.example.flowershopapp.ComposeUI.User

import android.app.DatePickerDialog
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.flowershopapp.ComposeUI.AppViewModelProvider
import com.example.flowershopapp.ComposeUI.Navigation.Screen
import com.example.flowershopapp.Entities.Model.OrderByDate
import com.example.flowershopapp.R
import java.util.Calendar

@Composable
fun Signup(
    navController: NavController,
   viewModel: UserViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var dateOfBirth by remember { mutableStateOf("Дата рождения") }
    var phoneNumber by remember { mutableStateOf("") }

    var usernameError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var dateOfBirthError by remember { mutableStateOf<String?>(null) }
    var phoneNumberError by remember { mutableStateOf<String?>(null) }

    var width by remember { mutableStateOf(0.dp) }


    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val dateOfBirthDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            dateOfBirth = "$year-${month + 1}-$dayOfMonth"
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)
    )

    fun validateInput(): Boolean {
        var isValid = true

        if (username.isBlank()) {
            usernameError = "Введите имя пользователя"
            isValid = false
        } else {
            usernameError = null
        }

        if (password.isBlank()) {
            passwordError = "Введите пароль"
            isValid = false
        } else {
            passwordError = null
        }

        if (dateOfBirth.isBlank()) {
            dateOfBirthError = "Введите дату рождения"
            isValid = false
        } else {
            dateOfBirthError = null
        }

        if (phoneNumber.isBlank()) {
            phoneNumberError = "Введите номер телефона"
            isValid = false
        } else {
            phoneNumberError = null
        }

        return isValid
    }

    fun register() {
        if (validateInput()) {
            viewModel.registerUser(username, password, dateOfBirth, phoneNumber){
                navController.navigate(Screen.Login.route)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.backgroundWindow)),
        verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically
        ) {
            Image(painter = painterResource(id = R.drawable.logo), contentDescription = "", Modifier.size(180.dp))
        }
        Column(
            modifier = Modifier
                .wrapContentWidth()
                .weight(2f)
                .padding(bottom = 10.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.signup_title),
                fontSize = 26.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                modifier = Modifier
                    .onGloballyPositioned { coordinates ->
                        width = coordinates.size.width.dp
                    },
                value = username,
                onValueChange = { username = it },
                label = { Text("Имя пользователя") },
                isError = usernameError != null,
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = colorResource(id = R.color.textFieldContainer),
                    focusedContainerColor = colorResource(id = R.color.textFieldContainer)
                )
            )
            if (usernameError != null) {
                Text(text = usernameError!!, color = Color.Red)
            }
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = { Text("Номер телефона") },
                isError = phoneNumberError != null,
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = colorResource(id = R.color.textFieldContainer),
                    focusedContainerColor = colorResource(id = R.color.textFieldContainer)
                )
            )
            if (phoneNumberError != null) {
                Text(text = phoneNumberError!!, color = Color.Red)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .width(width)
                    .background(colorResource(id = R.color.textFieldContainer))
                    .clickable { dateOfBirthDialog.show() }

            ) {
                Text(dateOfBirth, fontSize = 16.sp, color = Color.Black)
            }
            if (dateOfBirthError != null) {
                Text(text = dateOfBirthError!!, color = Color.Red)
            }
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Пароль") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = colorResource(id = R.color.textFieldContainer),
                    focusedContainerColor = colorResource(id = R.color.textFieldContainer)
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                shape = RoundedCornerShape(5.dp),
                onClick = { register() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.button)
            )) {
                Text("Создать")
            }
            Spacer(modifier = Modifier.height(16.dp))
            TextButton(onClick = { navController.navigate(Screen.Login.route) }) {
                Text(text = "У меня есть учетная запись")
            }
        }
    }
}
