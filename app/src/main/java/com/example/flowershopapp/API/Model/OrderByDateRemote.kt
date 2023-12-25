package com.example.flowershopapp.API.Model

import com.example.flowershopapp.Entities.Model.OrderByDate
import kotlinx.serialization.Serializable

@Serializable
data class OrderByDateRemote(
    val orders: List<OrderRemote> = listOf(),
    val orderCount: Int,
    val totalSum: Int
)

fun OrderByDateRemote.toOrderByDate(): OrderByDate = OrderByDate(
    orders.map { it.toOrder() },
    orderCount,
    totalSum
)

fun OrderByDate.toOrderByDateRemote(): OrderByDateRemote = OrderByDateRemote(
    orders.map { it.toOrderRemote() },
    orderCount,
    totalSum
)