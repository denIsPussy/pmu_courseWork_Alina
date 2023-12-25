package com.example.flowershopapp.API.Model

import com.example.flowershopapp.Entities.Model.OrderBouquetCrossRef
import kotlinx.serialization.Serializable

@Serializable
data class OrderBouquetCrossRefRemote(
    val id: Int = 0,
    val orderId: Int = 0,
    val bouquetId: Int = 0,
    val count: Int = 0,
)

fun OrderBouquetCrossRefRemote.OrderBouquetCrossRef(): OrderBouquetCrossRef = OrderBouquetCrossRef(
    orderId,
    bouquetId,
    count
)

fun OrderBouquetCrossRef.OrderBouquetCrossRefRemote(): OrderBouquetCrossRefRemote = OrderBouquetCrossRefRemote(
    orderId,
    bouquetId,
    count
)