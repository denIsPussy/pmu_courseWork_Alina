package com.example.flowershopapp.API.Model

import com.example.flowershopapp.Entities.Model.Order
import kotlinx.serialization.Serializable

@Serializable
data class OrderRemote(
    val id: Int? = null,
    val date: String = "",
    val sum: Int = 0,
    val userId: Int = 0,
)

fun OrderRemote.toOrder(): Order = Order(
    id,
    date,
    sum,
    userId
)

fun Order.toOrderRemote(): OrderRemote = OrderRemote(
    null,
    date,
    sum,
    userId
)