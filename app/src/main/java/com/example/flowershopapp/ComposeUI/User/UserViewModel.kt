package com.example.flowershopapp.ComposeUI.User

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flowershopapp.ComposeUI.Network.NetworkViewModel
import com.example.flowershopapp.Entities.Model.User
import com.example.flowershopapp.Entities.Repository.User.UserRepository
import kotlinx.coroutines.launch

class UserViewModel(
    private val userRepository: UserRepository
) : NetworkViewModel() {
    var userUiState by mutableStateOf(UserUiState())
        private set

    fun loginUser(username: String, password: String, onResult: (User?) -> Unit) {
        runInScope(
            actionSuccess = {
                userRepository.getAll()
                val user = userRepository.getUserByName(username)
                if (user.password == password) {
                    onResult(user)
                } else {
                    onResult(null)
                }
            }
        )
    }


    fun registerUser(
        username: String,
        password: String,
        dateOfBirth: String,
        phoneNumber: String,
        relocate: () -> Unit
    ) {
        runInScope(
            actionSuccess = {
                val user = userRepository.insert(User(username, dateOfBirth, phoneNumber, password))
                relocate()
            }
        )
    }
}

data class UserUiState(
    val userDetails: UserDetails = UserDetails()
)

data class UserDetails(
    val user: User? = null,
)

data class AuthenticationState(
    val isLoggedIn: Boolean = false,
    val errorMessage: String? = null
)

fun User.toDetails(): UserDetails = UserDetails(
    user = User(userId, userName, dateOfBirth, phoneNumber, password)
)

fun User.toUiState(): UserUiState = UserUiState(
    userDetails = this.toDetails()
)

fun UserUiState.toUser(): User = User(
    userId = userDetails.user!!.userId,
    userName = userDetails.user.userName,
    dateOfBirth = userDetails.user.dateOfBirth,
    phoneNumber = userDetails.user.phoneNumber,
    password = userDetails.user.password
)
