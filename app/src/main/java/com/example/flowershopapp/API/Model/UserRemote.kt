package com.example.flowershopapp.API.Model

import com.example.flowershopapp.Entities.Model.User
import kotlinx.serialization.Serializable

@Serializable
data class UserRemote(
    val id: Int = 0,
    val userName: String = "",
    val dateOfBirth: String = "",
    val phoneNumber: String = "",
    val password: String = ""
)

fun UserRemote.toUser(): User = User(
    id,
    userName,
    dateOfBirth,
    phoneNumber,
    password
)

fun User.toUserRemote(): UserRemote = UserRemote(
    userId ?: 0,
    userName,
    dateOfBirth,
    phoneNumber,
    password
)