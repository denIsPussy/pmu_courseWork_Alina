package com.example.flowershopapp.API.Model

import com.example.flowershopapp.Entities.Model.Bouquet
import com.example.flowershopapp.Entities.Model.OrderBouquetCrossRef
import com.example.flowershopapp.Entities.Model.OrdersWithBouquets
import kotlinx.serialization.Serializable

@Serializable
data class OrderBouquetRemote(
    val order: OrderRemote = OrderRemote(),
    val bouquet: List<BouquetRemote> = listOf()
)

fun OrderBouquetRemote.toOrdersWithBouquets(): OrdersWithBouquets {
    val convertedOrder = this.order.toOrder()
    val convertedBouquets = this.bouquet.map { it.toBouquet() }
    return OrdersWithBouquets(convertedOrder, convertedBouquets)
}

fun OrdersWithBouquets.toOrderBouquetRemote(): OrderBouquetRemote {
    val convertedOrder = this.order.toOrderRemote()
    val convertedBouquets = this.bouquets.map { it.toBouquetRemote() }
    return OrderBouquetRemote(convertedOrder, convertedBouquets)
}