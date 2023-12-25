package com.example.flowershopapp.API.Model

import com.example.flowershopapp.Entities.Model.OrderByDate
import kotlinx.serialization.Serializable

@Serializable
data class OrderByDateRemote(
    val orderCount: Int,
    val totalSum: Int
)

fun OrderByDateRemote.toOrderByDate(): OrderByDate = OrderByDate(
    orderCount,
    totalSum
)

fun OrderByDate.toOrderByDateRemote(): OrderByDateRemote = OrderByDateRemote(
    orderCount,
    totalSum
)